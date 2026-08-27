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
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val saveItemUseCase: SaveItemUseCase,
    private val textExtractor: TextExtractor,
    private val mediaStorage: MediaStorage,
    private val metadataWorkScheduler: MetadataWorkScheduler,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    fun saveSharedContent(sharedData: SharedData, onComplete: () -> Unit) {
        viewModelScope.launch {
            val localImageUris = mediaStorage.importImages(sharedData.imageUris)
            val extractedText = localImageUris.mapNotNull { rawUri ->
                runCatching {
                    textExtractor.extractFromUri(appContext, Uri.parse(rawUri))
                }.getOrNull()
            }.filter(String::isNotBlank)
                .joinToString(separator = "\n\n")
                .ifBlank { null }

            val item = UniBoxItem(
                title = sharedData.subject
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

            val savedId = saveItemUseCase(item)

            // If the item has a URL, enqueue a WorkManager job to fetch OpenGraph metadata
            if (sharedData.url != null) {
                metadataWorkScheduler.enqueue(savedId)
            }

            onComplete()
        }
    }

}
