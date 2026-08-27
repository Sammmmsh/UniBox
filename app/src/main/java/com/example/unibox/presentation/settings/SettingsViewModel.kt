package com.example.unibox.presentation.settings

import android.net.Uri
import com.example.unibox.data.export.LibraryExporter
import kotlinx.coroutines.CancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibox.domain.repository.UniBoxRepository
import com.example.unibox.domain.repository.WebPreviewPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Settings screen.
 * UX fix #10: Easy data export and clear data — findable in under 3 taps.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: UniBoxRepository,
    private val libraryExporter: LibraryExporter,
    private val themePreferences: com.example.unibox.domain.repository.ThemePreferences,
    private val webPreviewPreferences: WebPreviewPreferences
) : ViewModel() {

    private val _exportStatus = MutableStateFlow<String?>(null)
    val exportStatus: StateFlow<String?> = _exportStatus.asStateFlow()

    private val _clearStatus = MutableStateFlow<String?>(null)
    val clearStatus: StateFlow<String?> = _clearStatus.asStateFlow()

    private val _webPreviewStatus = MutableStateFlow<String?>(null)
    val webPreviewStatus: StateFlow<String?> = _webPreviewStatus.asStateFlow()

    val itemCount: StateFlow<Int> = repository.getItemCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val themeMode: StateFlow<com.example.unibox.domain.model.ThemeMode> = themePreferences.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = com.example.unibox.domain.model.ThemeMode.SYSTEM
        )

    val firecrawlEnabled: StateFlow<Boolean> = webPreviewPreferences.firecrawlEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val hasFirecrawlApiKey: StateFlow<Boolean> = webPreviewPreferences.hasFirecrawlApiKey
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun saveFirecrawlApiKey(apiKey: String?) {
        viewModelScope.launch {
            try {
                webPreviewPreferences.setFirecrawlApiKey(apiKey)
                _webPreviewStatus.value = if (apiKey.isNullOrBlank()) {
                    "Personal key removed"
                } else {
                    "Personal key saved securely on this device"
                }
            } catch (exception: Exception) {
                _webPreviewStatus.value = "The key could not be saved securely. Please try again."
            }
        }
    }

    fun setThemeMode(mode: com.example.unibox.domain.model.ThemeMode) {
        viewModelScope.launch {
            themePreferences.saveThemeMode(mode)
        }
    }

    fun setFirecrawlEnabled(enabled: Boolean) {
        viewModelScope.launch {
            webPreviewPreferences.setFirecrawlEnabled(enabled)
            _webPreviewStatus.value = if (enabled) {
                "Enhanced web previews enabled"
            } else {
                "Using direct page previews"
            }
        }
    }

    private val _isExporting = MutableStateFlow(false)
    val isExporting = _isExporting.asStateFlow()

    fun exportData(destination: Uri?) {
        if (destination == null || _isExporting.value) return
        _isExporting.value = true
        viewModelScope.launch {
            try {
                val count = libraryExporter.exportTo(destination)
                _exportStatus.value = "Exported $count items to your selected file"
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _exportStatus.value = "Export failed. Choose a writable location and try again."
            } finally {
                _isExporting.value = false
            }
        }
    }

    fun exportPickerUnavailable() {
        _exportStatus.value = "No file picker is available on this device."
    }

    fun clearAllData() {
        viewModelScope.launch {
            try {
                repository.deleteAllItems()
                _clearStatus.value = "All data cleared"
            } catch (e: Exception) {
                _clearStatus.value = "Failed to clear data: ${e.message}"
            }
        }
    }

    fun clearExportStatus() {
        _exportStatus.value = null
    }

    fun clearClearStatus() {
        _clearStatus.value = null
    }

    fun clearWebPreviewStatus() {
        _webPreviewStatus.value = null
    }
}
