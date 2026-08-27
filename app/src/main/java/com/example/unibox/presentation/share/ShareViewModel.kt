package com.example.unibox.presentation.share

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibox.data.media.MediaStorage
import com.example.unibox.data.workers.MetadataWorkScheduler
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.domain.model.WebEnrichmentStatus
import com.example.unibox.domain.usecase.SaveItemUseCase
import com.example.unibox.ml.TextExtractor
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class ShareSaveState(
    val isSaving: Boolean = false,
    val error: String? = null,
    val savedItemId: Long? = null
)

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val saveItemUseCase: SaveItemUseCase,
    private val textExtractor: TextExtractor,
    private val mediaStorage: MediaStorage,
    private val metadataWorkScheduler: MetadataWorkScheduler,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _saveState = MutableStateFlow(ShareSaveState())
    val saveState = _saveState.asStateFlow()

    fun saveSharedContent(sharedData: SharedData) {
        if (_saveState.value.isSaving || _saveState.value.savedItemId != null) return
        if (!sharedData.hasSaveableContent) {
            _saveState.value = ShareSaveState(error = "Share some text, a link, or an image to save it here.")
            return
        }
        _saveState.value = ShareSaveState(isSaving = true)
        viewModelScope.launch {
            var localImageUris = emptyList<String>()
            var committed = false
            try {
                localImageUris = mediaStorage.importImages(sharedData.imageUris)
                val extractedText = localImageUris.mapNotNull { rawUri ->
                    textExtractor.extractFromUri(appContext, Uri.parse(rawUri))
                }.filter(String::isNotBlank)
                    .joinToString(separator = "\n\n")
                    .ifBlank { null }

                val item = UniBoxItem(
                    title = sharedData.subject?.takeIf(String::isNotBlank)
                        ?: sharedData.url
                        ?: extractedText?.take(80)
                        ?: sharedData.rawText.take(80).ifBlank {
                            if (localImageUris.size > 1) "${localImageUris.size} shared images"
                            else "Shared content"
                        },
                    description = sharedData.rawText.takeIf {
                        it != (sharedData.subject ?: sharedData.url ?: it.take(80))
                    }.orEmpty(),
                    url = sharedData.url,
                    imageUri = localImageUris.firstOrNull(),
                    imageUris = localImageUris,
                    extractedText = extractedText,
                    sourceApp = sharedData.sourcePackage,
                    timestamp = System.currentTimeMillis(),
                    enrichmentStatus = if (sharedData.url != null) {
                        WebEnrichmentStatus.PENDING
                    } else {
                        WebEnrichmentStatus.NOT_REQUIRED
                    }
                )

                currentCoroutineContext().ensureActive()
                // Once the database commit starts, finish it before deciding whether media can be removed.
                // Cancellation during a Room insert must not leave a saved row pointing at deleted images.
                withContext(NonCancellable) {
                    val savedId = saveItemUseCase(item)
                    committed = true

                    if (sharedData.url != null) {
                        // Preview scheduling is best-effort: the saved item must never be inserted twice.
                        runCatching { metadataWorkScheduler.enqueue(savedId) }
                    }
                    _saveState.value = ShareSaveState(savedItemId = savedId)
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _saveState.value = ShareSaveState(
                    error = "Could not save this content. Nothing was added. Try again, or share it again from the source app."
                )
            } finally {
                if (!committed && localImageUris.isNotEmpty()) {
                    withContext(NonCancellable) { mediaStorage.deleteImages(localImageUris) }
                }
            }
        }
    }

}
