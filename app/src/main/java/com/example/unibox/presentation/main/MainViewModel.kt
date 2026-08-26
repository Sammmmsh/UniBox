package com.example.unibox.presentation.main

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.unibox.data.workers.MetadataWorker
import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.ItemStatus
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.domain.repository.UniBoxRepository
import com.example.unibox.domain.usecase.DeleteItemUseCase
import com.example.unibox.domain.usecase.GetItemsUseCase
import com.example.unibox.domain.usecase.SaveItemUseCase
import com.example.unibox.domain.usecase.SearchItemsUseCase
import com.example.unibox.domain.usecase.UpdateItemUseCase
import com.example.unibox.util.ConnectivityObserver
import com.example.unibox.util.SmartReviewManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class LibrarySection(val label: String) {
    INBOX("Inbox"),
    LIBRARY("Library"),
    ARCHIVE("Archive")
}

enum class ItemSortOrder(val label: String) {
    NEWEST("Newest first"),
    OLDEST("Oldest first"),
    TITLE("Title")
}

data class MainUiState(
    val items: List<UniBoxItem> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: Category? = null,
    val section: LibrarySection = LibrarySection.INBOX,
    val sortOrder: ItemSortOrder = ItemSortOrder.NEWEST,
    val favoriteOnly: Boolean = false,
    val selectedCollection: String? = null,
    val collections: List<String> = emptyList(),
    val isLoading: Boolean = true
) {
    val activeFilterCount: Int
        get() = listOf(
            favoriteOnly,
            selectedCollection != null,
            sortOrder != ItemSortOrder.NEWEST
        ).count { it }
}

private data class WorkflowFilters(
    val section: LibrarySection,
    val sortOrder: ItemSortOrder,
    val favoriteOnly: Boolean,
    val selectedCollection: String?
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getItemsUseCase: GetItemsUseCase,
    private val searchItemsUseCase: SearchItemsUseCase,
    private val saveItemUseCase: SaveItemUseCase,
    private val updateItemUseCase: UpdateItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val repository: UniBoxRepository,
    @ApplicationContext private val appContext: Context,
    connectivityObserver: ConnectivityObserver,
    private val smartReviewManager: SmartReviewManager
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = connectivityObserver.isOnline

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow<Category?>(null)
    private val _section = MutableStateFlow(LibrarySection.INBOX)
    private val _sortOrder = MutableStateFlow(ItemSortOrder.NEWEST)
    private val _favoriteOnly = MutableStateFlow(false)
    private val _selectedCollection = MutableStateFlow<String?>(null)

    private val baseItems = combine(
        _searchQuery.debounce(300),
        _selectedCategory
    ) { query, category -> query to category }
        .flatMapLatest { (query, category) ->
            if (query.isBlank()) getItemsUseCase(category)
            else searchItemsUseCase(query, category)
        }

    private val workflowFilters = combine(
        _section,
        _sortOrder,
        _favoriteOnly,
        _selectedCollection
    ) { section, sortOrder, favoriteOnly, collection ->
        WorkflowFilters(section, sortOrder, favoriteOnly, collection)
    }

    val uiState: StateFlow<MainUiState> = combine(
        baseItems,
        workflowFilters,
        _searchQuery,
        _selectedCategory,
        repository.getCollectionNames()
    ) { items, filters, query, category, collections ->
        val visibleItems = items.applyWorkflow(
            section = filters.section,
            sortOrder = filters.sortOrder,
            favoriteOnly = filters.favoriteOnly,
            selectedCollection = filters.selectedCollection
        )

        MainUiState(
            items = visibleItems,
            searchQuery = query,
            selectedCategory = category,
            section = filters.section,
            sortOrder = filters.sortOrder,
            favoriteOnly = filters.favoriteOnly,
            selectedCollection = filters.selectedCollection,
            collections = collections,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainUiState()
    )

    init {
        smartReviewManager.incrementSessionCount()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: Category?) {
        _selectedCategory.value = category
    }

    fun onSectionSelected(section: LibrarySection) {
        _section.value = section
    }

    fun onSortOrderSelected(sortOrder: ItemSortOrder) {
        _sortOrder.value = sortOrder
    }

    fun onFavoriteOnlyChanged(enabled: Boolean) {
        _favoriteOnly.value = enabled
    }

    fun onCollectionSelected(collection: String?) {
        _selectedCollection.value = collection
    }

    fun clearWorkflowFilters() {
        _sortOrder.value = ItemSortOrder.NEWEST
        _favoriteOnly.value = false
        _selectedCollection.value = null
    }

    fun toggleFavorite(item: UniBoxItem) {
        viewModelScope.launch {
            updateItemUseCase(item.copy(isFavorite = !item.isFavorite))
        }
    }

    fun moveToLibrary(item: UniBoxItem) {
        viewModelScope.launch {
            updateItemUseCase(item.copy(status = ItemStatus.SAVED, snoozedUntil = null))
        }
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch { deleteItemUseCase(id) }
    }

    fun saveManualItem(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            val url = Regex("https?://\\S+", RegexOption.IGNORE_CASE).find(text)?.value
            val item = UniBoxItem(
                title = url ?: text.take(80),
                description = if (url != null) text else "",
                url = url,
                category = categorizeText(text),
                sourceApp = "Manual Entry"
            )

            val savedId = saveItemUseCase(item)
            smartReviewManager.incrementSaveCount()

            if (url != null) {
                val request = OneTimeWorkRequestBuilder<MetadataWorker>()
                    .setInputData(workDataOf(MetadataWorker.KEY_ITEM_ID to savedId))
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .build()
                WorkManager.getInstance(appContext).enqueue(request)
            }
        }
    }

    private fun categorizeText(text: String): Category {
        val lowerText = text.lowercase()
        return when {
            lowerText.containsAny("recipe", "cook", "bake", "ingredient", "preheat", "allrecipes.com") -> Category.RECIPE
            lowerText.containsAny("restaurant", "pizza", "sushi", "cafe", "brunch", "food", "yelp.com", "doordash.com", "ubereats.com") -> Category.FOOD
            lowerText.containsAny("flight", "hotel", "travel", "trip", "airport", "booking.com", "airbnb.com", "tripadvisor.com") -> Category.TRAVEL
            lowerText.containsAny("kotlin", "android", "api", "code", "programming", "github.com", "stackoverflow.com", "dev.to", "medium.com") -> Category.TECH
            lowerText.containsAny("buy", "sale", "discount", "price", "shop", "amazon.com", "ebay.com", "etsy.com", "nike.com") -> Category.SHOPPING
            lowerText.containsAny("watch", "video", "trailer", "episode", "youtube.com", "youtu.be", "vimeo.com", "tiktok.com") -> Category.VIDEO
            lowerText.containsAny("song", "album", "playlist", "artist", "spotify.com", "soundcloud.com", "music.apple.com") -> Category.MUSIC
            lowerText.containsAny("twitter.com", "x.com", "instagram.com", "reddit.com") -> Category.SOCIAL
            else -> Category.UNCATEGORIZED
        }
    }

    private fun String.containsAny(vararg keywords: String): Boolean {
        return keywords.any(::contains)
    }

    fun requestReviewIfEligible(activity: Activity) {
        if (smartReviewManager.shouldRequestReview()) {
            smartReviewManager.requestReview(activity)
        }
    }
}
