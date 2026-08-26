package com.example.unibox.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.ItemStatus
import com.example.unibox.domain.repository.UniBoxRepository
import com.example.unibox.location.GeofenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val item: UniBoxItem? = null,
    val isLoading: Boolean = true,
    val geofenceStatus: String? = null,
    val collections: List<String> = emptyList(),
    val actionMessage: String? = null
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: UniBoxRepository,
    private val geofenceManager: GeofenceManager
) : ViewModel() {

    private val itemId: Long = savedStateHandle.get<Long>("itemId") ?: -1L

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getItemById(itemId).collect { item ->
                _uiState.update { it.copy(item = item, isLoading = false) }
            }
        }
        viewModelScope.launch {
            repository.getCollectionNames().collect { collections ->
                _uiState.update { it.copy(collections = collections) }
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

    fun updateItemDetails(
        title: String,
        description: String,
        userNote: String,
        category: Category,
        collectionName: String?,
        tags: List<String>
    ) = updateCurrentItem("Changes saved") { item ->
        item.copy(
            title = title.trim().ifBlank { item.title },
            description = description.trim(),
            userNote = userNote.trim(),
            category = category,
            collectionName = collectionName?.trim()?.takeIf(String::isNotBlank),
            tags = tags.map(String::trim)
                .filter(String::isNotBlank)
                .distinctBy { it.lowercase() }
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
