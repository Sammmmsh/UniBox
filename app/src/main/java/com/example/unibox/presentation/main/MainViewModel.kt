package com.example.unibox.presentation.main

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibox.data.workers.MetadataWorkScheduler
import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.domain.model.WebEnrichmentStatus
import com.example.unibox.domain.repository.UniBoxRepository
import com.example.unibox.domain.usecase.DeleteItemUseCase
import com.example.unibox.domain.usecase.GetItemsUseCase
import com.example.unibox.domain.usecase.SaveItemUseCase
import com.example.unibox.domain.usecase.SearchItemsUseCase
import com.example.unibox.util.ConnectivityObserver
import com.example.unibox.util.SmartReviewManager
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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
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
    val isLoading: Boolean = true,
    val isSearching: Boolean = false,
    val sectionItemCount: Int = 0,
    val sectionCounts: Map<LibrarySection, Int> = emptyMap(),
    val availableCategories: List<Category> = emptyList(),
    val collectionSummaries: List<CollectionSummary> = emptyList(),
    val errorMessage: String? = null
) {
    val activeFilterCount: Int
        get() = listOf(
            selectedCategory != null,
            favoriteOnly,
            selectedCollection != null,
            sortOrder != ItemSortOrder.NEWEST
        ).count { it }

    val hasActiveFilters: Boolean get() = searchQuery.isNotBlank() || activeFilterCount > 0
}

data class ManualSaveState(val isSaving: Boolean = false, val error: String? = null, val savedItemId: Long? = null)
data class LibraryMessage(val text: String, val showLibrary: Boolean = false)

private data class ItemSearchResult(
    val items: List<UniBoxItem>,
    val query: String,
    val category: Category?,
    val error: String? = null
)

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
    private val deleteItemUseCase: DeleteItemUseCase,
    private val repository: UniBoxRepository,
    private val metadataWorkScheduler: MetadataWorkScheduler,
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
    private val _manualSaveState = MutableStateFlow(ManualSaveState())
    val manualSaveState = _manualSaveState.asStateFlow()
    private val _messages = Channel<LibraryMessage>(Channel.BUFFERED)
    val messages = _messages.receiveAsFlow()
    private val pendingActions = mutableSetOf<Long>()

    private val baseItems = combine(
        _searchQuery.debounce(300),
        _selectedCategory
    ) { query, category -> query to category }
        .flatMapLatest { (query, category) ->
            val source = if (query.isBlank()) getItemsUseCase(category)
            else searchItemsUseCase(query, category)
            source.map { ItemSearchResult(it, query, category) }.catch { error ->
                if (error is CancellationException) throw error
                emit(ItemSearchResult(emptyList(), query, category, "Could not load items. Change or clear the search to try again."))
            }
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
        repository.getAllItems()
    ) { result, filters, query, category, library ->
        val sectionItems = library.inSection(filters.section)
        val visibleItems = result.items.applyWorkflow(
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
            collections = sectionItems.collectionSummaries().map { it.name },
            isLoading = false,
            isSearching = query != result.query || category != result.category,
            sectionItemCount = sectionItems.size,
            sectionCounts = LibrarySection.entries.associateWith { library.inSection(it).size },
            availableCategories = sectionItems.availableCategories(category),
            collectionSummaries = sectionItems.collectionSummaries(),
            errorMessage = result.error
        )
    }.flowOn(Dispatchers.Default).stateIn(
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
        _searchQuery.value = ""
        _selectedCategory.value = null
        _sortOrder.value = ItemSortOrder.NEWEST
        _favoriteOnly.value = false
        _selectedCollection.value = null
    }

    fun toggleFavorite(item: UniBoxItem) {
        runItemAction(item.id) {
            if (repository.setFavorite(item.id, !item.isFavorite)) {
                _messages.send(LibraryMessage(if (item.isFavorite) "Removed from favorites" else "Added to favorites"))
            } else {
                _messages.send(LibraryMessage("This item is no longer available"))
            }
        }
    }

    fun moveToLibrary(item: UniBoxItem) {
        runItemAction(item.id) {
            if (repository.saveToLibrary(item.id)) {
                _messages.send(LibraryMessage("Saved to library", showLibrary = true))
            } else {
                _messages.send(LibraryMessage("This item has already moved"))
            }
        }
    }

    private fun runItemAction(id: Long, action: suspend () -> Unit) {
        if (!pendingActions.add(id)) return
        viewModelScope.launch {
            try {
                action()
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _messages.send(LibraryMessage("Could not save that change. Please try again."))
            } finally {
                pendingActions.remove(id)
            }
        }
    }

    fun showSavedItems() {
        clearWorkflowFilters()
        _section.value = LibrarySection.LIBRARY
    }

    fun clearCaptureError() {
        _manualSaveState.value = _manualSaveState.value.copy(error = null)
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch { deleteItemUseCase(id) }
    }

    fun saveManualItem(text: String) {
        if (text.isBlank() || _manualSaveState.value.isSaving) return
        _manualSaveState.value = _manualSaveState.value.copy(isSaving = true, error = null)

        viewModelScope.launch {
            try {
                val url = Regex("https?://\\S+", RegexOption.IGNORE_CASE).find(text)?.value
                val item = UniBoxItem(
                    title = url ?: text.take(80),
                    description = if (url != null || text.length > 80) text else "",
                    url = url,
                    sourceApp = "Manual Entry",
                    enrichmentStatus = if (url != null) {
                        WebEnrichmentStatus.PENDING
                    } else {
                        WebEnrichmentStatus.NOT_REQUIRED
                    }
                )

                val savedId = saveItemUseCase(item)
                smartReviewManager.incrementSaveCount()

                if (url != null) {
                    // The note is already saved even if preview scheduling is temporarily unavailable.
                    runCatching { metadataWorkScheduler.enqueue(savedId) }
                }
                _manualSaveState.value = ManualSaveState(savedItemId = savedId)
                _messages.send(LibraryMessage("Saved to inbox"))
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _manualSaveState.value = _manualSaveState.value.copy(
                    isSaving = false, error = "Could not save. Your draft is still here."
                )
            }
        }
    }

    fun requestReviewIfEligible(activity: Activity) {
        if (smartReviewManager.shouldRequestReview()) {
            smartReviewManager.requestReview(activity)
        }
    }
}
