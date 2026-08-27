package com.example.unibox.presentation.settings

import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibox.domain.repository.UniBoxRepository
import com.example.unibox.domain.repository.WebPreviewPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

/**
 * ViewModel for the Settings screen.
 * UX fix #10: Easy data export and clear data — findable in under 3 taps.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: UniBoxRepository,
    @ApplicationContext private val context: Context,
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

    fun exportData() {
        viewModelScope.launch {
            try {
                val items = repository.getAllItemsSync()
                val jsonArray = JSONArray()

                for (item in items) {
                    val obj = JSONObject().apply {
                        put("id", item.id)
                        put("title", item.title)
                        put("description", item.description)
                        put("url", item.url ?: JSONObject.NULL)
                        put("thumbnailUrl", item.thumbnailUrl ?: JSONObject.NULL)
                        put("extractedText", item.extractedText ?: JSONObject.NULL)
                        put("category", item.category.name)
                        put("sourceApp", item.sourceApp ?: JSONObject.NULL)
                        put("timestamp", item.timestamp)
                        put("latitude", item.latitude ?: JSONObject.NULL)
                        put("longitude", item.longitude ?: JSONObject.NULL)
                        put("locationLabel", item.locationLabel ?: JSONObject.NULL)
                        put("imageUri", item.imageUri ?: JSONObject.NULL)
                        put("imageUris", JSONArray(item.imageUris))
                        put("status", item.status.name)
                        put("isFavorite", item.isFavorite)
                        put("snoozedUntil", item.snoozedUntil ?: JSONObject.NULL)
                        put("userNote", item.userNote)
                        put("collectionName", item.collectionName ?: JSONObject.NULL)
                        put("tags", JSONArray(item.tags))
                        put("organizationReviewed", item.organizationReviewed)
                        put("enrichmentStatus", item.enrichmentStatus.name)
                        put("enrichmentProvider", item.enrichmentProvider ?: JSONObject.NULL)
                        put("enrichmentError", item.enrichmentError ?: JSONObject.NULL)
                        put("canonicalUrl", item.canonicalUrl ?: JSONObject.NULL)
                        put("webSiteName", item.webSiteName ?: JSONObject.NULL)
                        put("webAuthor", item.webAuthor ?: JSONObject.NULL)
                        put("webPublishedAt", item.webPublishedAt ?: JSONObject.NULL)
                        put("webLanguage", item.webLanguage ?: JSONObject.NULL)
                        put("webReadingTimeMinutes", item.webReadingTimeMinutes ?: JSONObject.NULL)
                        put("lastEnrichedAt", item.lastEnrichedAt ?: JSONObject.NULL)
                        put("updatedAt", item.updatedAt)
                    }
                    jsonArray.put(obj)
                }

                val docsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                val file = File(docsDir, "unibox_export.json")
                file.writeText(jsonArray.toString(2))

                _exportStatus.value = "Exported ${items.size} items to Documents/unibox_export.json"
            } catch (e: Exception) {
                _exportStatus.value = "Export failed: ${e.message}"
            }
        }
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
