package com.example.unibox.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibox.domain.model.UniBoxItem
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
    val geofenceStatus: String? = null
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
}
