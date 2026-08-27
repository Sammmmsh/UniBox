package com.example.unibox.data.export

import android.content.Context
import android.net.Uri
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.domain.repository.UniBoxRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/** Writes only library records, never settings or credentials, to a user-selected document. */
@Singleton
class LibraryExporter @Inject constructor(
    private val repository: UniBoxRepository,
    @ApplicationContext context: Context
) {
    private val resolver = context.contentResolver

    suspend fun exportTo(destination: Uri): Int = withContext(Dispatchers.IO) {
        val items = repository.getAllItemsSync()
        val json = libraryJson(items).toString(2)
        val output = resolver.openOutputStream(destination, "wt")
            ?: throw IOException("The selected document could not be opened")
        output.bufferedWriter(Charsets.UTF_8).use { it.write(json) }
        items.size
    }
}

/** Keeps the original JSON-array format so existing export consumers continue to work. */
internal fun libraryJson(items: List<UniBoxItem>): JSONArray = JSONArray().apply {
    items.forEach { item ->
        put(JSONObject().apply {
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
        })
    }
}
