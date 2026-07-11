package com.example.unibox.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.domain.usecase.DeleteItemUseCase
import com.example.unibox.domain.usecase.GetItemsUseCase
import com.example.unibox.domain.usecase.SaveItemUseCase
import com.example.unibox.domain.usecase.SearchItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.unibox.data.workers.MetadataWorker
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * UI state for the main inbox screen.
 * Follows Unidirectional Data Flow (UDF).
 */
data class MainUiState(
    val items: List<UniBoxItem> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: Category? = null,
    val isLoading: Boolean = true
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getItemsUseCase: GetItemsUseCase,
    private val searchItemsUseCase: SearchItemsUseCase,
    private val saveItemUseCase: SaveItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow<Category?>(null)

    /**
     * Reactive pipeline:
     * 1. Combine search query + category filter
     * 2. Debounce search input (300ms) to avoid hammering the DB
     * 3. flatMapLatest to cancel previous query when a new one arrives
     * 4. Map results into MainUiState
     */
    val uiState: StateFlow<MainUiState> = combine(
        _searchQuery.debounce(300),
        _selectedCategory
    ) { query, category ->
        Pair(query, category)
    }.flatMapLatest { (query, category) ->
        if (query.isBlank()) {
            getItemsUseCase(category)
        } else {
            searchItemsUseCase(query, category)
        }
    }.combine(
        combine(_searchQuery, _selectedCategory) { q, c -> Pair(q, c) }
    ) { items, (query, category) ->
        MainUiState(
            items = items,
            searchQuery = query,
            selectedCategory = category,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: Category?) {
        _selectedCategory.value = category
    }



    fun deleteItem(id: Long) {
        viewModelScope.launch {
            deleteItemUseCase(id)
        }
    }

    fun saveManualItem(text: String) {
        if (text.isBlank()) return
        
        viewModelScope.launch {
            // Extract URL if present
            val urlPattern = Regex("https?://\\S+", RegexOption.IGNORE_CASE)
            val url = urlPattern.find(text)?.value
            
            val item = UniBoxItem(
                title = url ?: text.take(80),
                description = if (url != null) text else "",
                url = url,
                category = categorizeText(text),
                sourceApp = "Manual Entry",
                timestamp = System.currentTimeMillis()
            )
            
            val savedId = saveItemUseCase(item)
            
            if (url != null) {
                val workRequest = OneTimeWorkRequestBuilder<MetadataWorker>()
                    .setInputData(workDataOf(MetadataWorker.KEY_ITEM_ID to savedId))
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .build()
                WorkManager.getInstance(appContext).enqueue(workRequest)
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
        return keywords.any { this.contains(it) }
    }
}
