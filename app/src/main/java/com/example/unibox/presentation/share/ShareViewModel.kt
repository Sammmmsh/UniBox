package com.example.unibox.presentation.share

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.unibox.data.workers.MetadataWorker
import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.UniBoxItem
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
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    fun saveSharedContent(sharedData: SharedData, onComplete: () -> Unit) {
        viewModelScope.launch {
            // If image shared, run ML Kit OCR first
            var extractedText: String? = null
            if (sharedData.imageUri != null) {
                try {
                    val uri = Uri.parse(sharedData.imageUri)
                    extractedText = textExtractor.extractFromUri(appContext, uri)
                } catch (_: Exception) { }
            }

            val item = UniBoxItem(
                title = sharedData.subject
                    ?: sharedData.url
                    ?: extractedText?.take(80)
                    ?: sharedData.rawText.take(80).ifBlank { "Shared content" },
                description = if (sharedData.subject != null) sharedData.rawText else "",
                url = sharedData.url,
                imageUri = sharedData.imageUri,
                extractedText = extractedText,
                category = categorize(sharedData, extractedText),
                sourceApp = sharedData.sourcePackage,
                timestamp = System.currentTimeMillis()
            )

            val savedId = saveItemUseCase(item)

            // If the item has a URL, enqueue a WorkManager job to fetch OpenGraph metadata
            if (sharedData.url != null) {
                enqueueMetadataFetch(savedId)
            }

            onComplete()
        }
    }

    private fun enqueueMetadataFetch(itemId: Long) {
        val workRequest = OneTimeWorkRequestBuilder<MetadataWorker>()
            .setInputData(workDataOf(MetadataWorker.KEY_ITEM_ID to itemId))
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(appContext)
            .enqueue(workRequest)
    }

    private fun categorize(data: SharedData, extractedText: String?): Category {
        val text = "${data.rawText} ${data.url ?: ""} ${data.subject ?: ""} ${extractedText ?: ""}".lowercase()

        return when {
            data.type == SharedDataType.IMAGE && data.url == null -> Category.BOOKMARK
            data.url != null -> categorizeUrl(data.url, text)
            else -> categorizeText(text)
        }
    }

    private fun categorizeUrl(url: String, text: String): Category {
        val u = url.lowercase()
        return when {
            u.contains("youtube.com") || u.contains("youtu.be")
                    || u.contains("vimeo.com") || u.contains("tiktok.com") -> Category.VIDEO
            u.contains("spotify.com") || u.contains("soundcloud.com")
                    || u.contains("music.apple.com") -> Category.MUSIC
            u.contains("amazon.com") || u.contains("ebay.com")
                    || u.contains("etsy.com") || u.contains("nike.com")
                    || u.contains("shop") -> Category.SHOPPING
            u.contains("yelp.com") || u.contains("doordash.com")
                    || u.contains("ubereats.com") -> Category.FOOD
            u.contains("booking.com") || u.contains("airbnb.com")
                    || u.contains("tripadvisor.com") -> Category.TRAVEL
            u.contains("github.com") || u.contains("stackoverflow.com")
                    || u.contains("dev.to") || u.contains("medium.com") -> Category.TECH
            u.contains("twitter.com") || u.contains("x.com")
                    || u.contains("instagram.com") || u.contains("reddit.com") -> Category.SOCIAL
            u.contains("allrecipes.com") || u.contains("recipe") -> Category.RECIPE
            else -> categorizeText(text)
        }
    }

    private fun categorizeText(text: String): Category {
        return when {
            text.containsAny("recipe", "cook", "bake", "ingredient", "preheat") -> Category.RECIPE
            text.containsAny("restaurant", "pizza", "sushi", "cafe", "brunch", "food") -> Category.FOOD
            text.containsAny("flight", "hotel", "travel", "trip", "airport") -> Category.TRAVEL
            text.containsAny("kotlin", "android", "api", "code", "programming") -> Category.TECH
            text.containsAny("buy", "sale", "discount", "price", "shop") -> Category.SHOPPING
            text.containsAny("watch", "video", "trailer", "episode") -> Category.VIDEO
            text.containsAny("song", "album", "playlist", "artist") -> Category.MUSIC
            else -> Category.UNCATEGORIZED
        }
    }

    private fun String.containsAny(vararg keywords: String): Boolean {
        return keywords.any { this.contains(it) }
    }
}
