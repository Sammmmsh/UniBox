package com.example.unibox.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.ItemStatus
import com.example.unibox.domain.model.WebEnrichmentStatus
import com.example.unibox.domain.repository.UniBoxRepository
import com.example.unibox.domain.organization.OrganizationEngine
import com.example.unibox.domain.organization.OrganizationSelection
import com.example.unibox.domain.organization.OrganizationSuggestions
import com.example.unibox.data.workers.MetadataWorkScheduler
import com.example.unibox.location.GeofenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val item: UniBoxItem? = null,
    val isLoading: Boolean = true,
    val geofenceStatus: String? = null,
    val collections: List<String> = emptyList(),
    val actionMessage: String? = null,
    val organizationSuggestions: OrganizationSuggestions = OrganizationSuggestions(),
    val isApplyingOrganization: Boolean = false
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: UniBoxRepository,
    private val geofenceManager: GeofenceManager,
    private val metadataWorkScheduler: MetadataWorkScheduler,
    private val organizationEngine: OrganizationEngine
) : ViewModel() {

    private val itemId: Long = savedStateHandle.get<Long>("itemId") ?: -1L

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllItems().collectLatest { library ->
                val item = library.firstOrNull { it.id == itemId }
                val suggestions = withContext(Dispatchers.Default) {
                    item?.let { organizationEngine.suggest(it, library) } ?: OrganizationSuggestions()
                }
                val collections = library.mapNotNull { it.collectionName }
                    .filter(String::isNotBlank).distinct().sortedBy { it.lowercase() }
                _uiState.update {
                    it.copy(
                        item = item,
                        isLoading = false,
                        collections = collections,
                        organizationSuggestions = suggestions
                    )
                }
            }
        }
    }

    fun applyOrganizationSuggestions(selection: OrganizationSelection) {
        val state = _uiState.value
        val item = state.item ?: return
        if (state.isApplyingOrganization || item.organizationReviewed) return
        val offered = state.organizationSuggestions
        val approved = OrganizationSelection(
            category = selection.category?.takeIf { it == offered.category?.value },
            tags = selection.tags.filter { tag -> offered.tags.any { it.value == tag } },
            collectionName = selection.collectionName?.takeIf { it == offered.collection?.value }
        )
        if (approved.isEmpty) return
        _uiState.update { it.copy(isApplyingOrganization = true) }
        viewModelScope.launch {
            try {
                val applied = repository.applyOrganizationSuggestions(item, approved)
                _uiState.update {
                    it.copy(actionMessage = if (applied) "Organization updated" else "Item changed. Review the latest suggestions.")
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { it.copy(actionMessage = "Could not apply suggestions. Please try again.") }
            } finally {
                _uiState.update { it.copy(isApplyingOrganization = false) }
            }
        }
    }

    fun dismissOrganizationSuggestions() = setOrganizationReviewed(true)

    fun reviewOrganizationSuggestions() = setOrganizationReviewed(false)

    private fun setOrganizationReviewed(reviewed: Boolean) {
        if (_uiState.value.isApplyingOrganization) return
        viewModelScope.launch {
            try {
                repository.setOrganizationReviewed(itemId, reviewed)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { it.copy(actionMessage = "Could not update suggestions. Please try again.") }
            }
        }
    }

    fun deleteItem(onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.deleteItem(itemId)
            geofenceManager.removeGeofence(itemId)
            onDeleted()
        }
    }

    fun attachGeofence(latitude: Double, longitude: Double) {
        val item = _uiState.value.item ?: return
        geofenceManager.addGeofence(
            itemId = item.id,
            itemTitle = item.title,
            latitude = latitude,
            longitude = longitude,
            onSuccess = {
                _uiState.update { it.copy(geofenceStatus = "Location reminder set!") }
            },
            onFailure = { e ->
                _uiState.update { it.copy(geofenceStatus = "Failed: ${e.message}") }
            }
        )

        // Also update the item in the DB with the location
        viewModelScope.launch {
            repository.updateItem(
                item.copy(latitude = latitude, longitude = longitude)
            )
        }
    }

    fun clearGeofenceStatus() {
        _uiState.update { it.copy(geofenceStatus = null) }
    }

    fun toggleFavorite() = updateCurrentItem(
        message = if (_uiState.value.item?.isFavorite == true) {
            "Removed from favorites"
        } else {
            "Added to favorites"
        }
    ) { item -> item.copy(isFavorite = !item.isFavorite) }

    fun toggleArchived() = updateCurrentItem(
        message = if (_uiState.value.item?.status == ItemStatus.ARCHIVED) {
            "Restored to library"
        } else {
            "Archived"
        }
    ) { item ->
        item.copy(
            status = if (item.status == ItemStatus.ARCHIVED) {
                ItemStatus.SAVED
            } else {
                ItemStatus.ARCHIVED
            },
            snoozedUntil = null
        )
    }

    fun moveToLibrary() = updateCurrentItem("Saved to library") { item ->
        item.copy(status = ItemStatus.SAVED, snoozedUntil = null)
    }

    fun snoozeUntil(timestamp: Long) = updateCurrentItem("Snoozed") { item ->
        item.copy(status = ItemStatus.INBOX, snoozedUntil = timestamp)
    }

    fun clearSnooze() = updateCurrentItem("Returned to inbox") { item ->
        item.copy(snoozedUntil = null)
    }

    fun retryWebPreview() {
        val item = _uiState.value.item?.takeIf { it.url != null } ?: return
        viewModelScope.launch {
            repository.updateItem(
                item.copy(
                    enrichmentStatus = WebEnrichmentStatus.PENDING,
                    enrichmentError = null,
                    updatedAt = System.currentTimeMillis()
                )
            )
            metadataWorkScheduler.enqueue(item.id, replaceExisting = true)
            _uiState.update { it.copy(actionMessage = "Refreshing web preview") }
        }
    }

    fun updateItemDetails(
        title: String,
        description: String,
        userNote: String,
        category: Category,
        collectionName: String?,
        tags: List<String>
    ) = updateCurrentItem("Changes saved") { item ->
        val normalizedCollection = collectionName?.trim()?.takeIf(String::isNotBlank)
        val normalizedTags = tags.map(String::trim)
            .filter(String::isNotBlank)
            .distinctBy { it.lowercase() }
        item.copy(
            title = title.trim().ifBlank { item.title },
            description = description.trim(),
            userNote = userNote.trim(),
            category = category,
            collectionName = normalizedCollection,
            tags = normalizedTags,
            organizationReviewed = item.organizationReviewed ||
                category != item.category || normalizedCollection != item.collectionName ||
                normalizedTags != item.tags
        )
    }

    fun clearActionMessage() {
        _uiState.update { it.copy(actionMessage = null) }
    }

    private fun updateCurrentItem(
        message: String,
        transform: (UniBoxItem) -> UniBoxItem
    ) {
        val item = _uiState.value.item ?: return
        viewModelScope.launch {
            repository.updateItem(
                transform(item).copy(updatedAt = System.currentTimeMillis())
            )
            _uiState.update { it.copy(actionMessage = message) }
        }
    }
}
