package com.example.unibox.data.local

import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.ItemStatus
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.domain.model.WebEnrichmentStatus
import org.json.JSONArray

// Mapping functions between Room entities and domain models.
// Keeps the domain layer completely independent of Room annotations.

fun UniBoxItemEntity.toDomainModel(): UniBoxItem {
    return UniBoxItem(
        id = id,
        title = title,
        description = description,
        url = url,
        thumbnailUrl = thumbnailUrl,
        extractedText = extractedText,
        category = try {
            Category.valueOf(category)
        } catch (e: IllegalArgumentException) {
            Category.UNCATEGORIZED
        },
        sourceApp = sourceApp,
        timestamp = timestamp,
        latitude = latitude,
        longitude = longitude,
        locationLabel = locationLabel,
        imageUri = imageUri,
        imageUris = imageUrisJson.toStringList(),
        status = runCatching { ItemStatus.valueOf(status) }.getOrDefault(ItemStatus.INBOX),
        isFavorite = isFavorite,
        snoozedUntil = snoozedUntil,
        userNote = userNote,
        collectionName = collectionName,
        tags = tagsJson.toStringList(),
        enrichmentStatus = runCatching {
            WebEnrichmentStatus.valueOf(enrichmentStatus)
        }.getOrDefault(WebEnrichmentStatus.NOT_REQUIRED),
        enrichmentProvider = enrichmentProvider,
        enrichmentError = enrichmentError,
        canonicalUrl = canonicalUrl,
        webSiteName = webSiteName,
        webAuthor = webAuthor,
        webPublishedAt = webPublishedAt,
        webLanguage = webLanguage,
        webReadingTimeMinutes = webReadingTimeMinutes,
        lastEnrichedAt = lastEnrichedAt,
        updatedAt = updatedAt
    )
}

fun UniBoxItem.toEntity(): UniBoxItemEntity {
    return UniBoxItemEntity(
        id = id,
        title = title,
        description = description,
        url = url,
        thumbnailUrl = thumbnailUrl,
        extractedText = extractedText,
        category = category.name,
        sourceApp = sourceApp,
        timestamp = timestamp,
        latitude = latitude,
        longitude = longitude,
        locationLabel = locationLabel,
        imageUri = imageUri,
        imageUrisJson = imageUris.toJsonArray(),
        status = status.name,
        isFavorite = isFavorite,
        snoozedUntil = snoozedUntil,
        userNote = userNote,
        collectionName = collectionName,
        tagsJson = tags.toJsonArray(),
        enrichmentStatus = enrichmentStatus.name,
        enrichmentProvider = enrichmentProvider,
        enrichmentError = enrichmentError,
        canonicalUrl = canonicalUrl,
        webSiteName = webSiteName,
        webAuthor = webAuthor,
        webPublishedAt = webPublishedAt,
        webLanguage = webLanguage,
        webReadingTimeMinutes = webReadingTimeMinutes,
        lastEnrichedAt = lastEnrichedAt,
        updatedAt = updatedAt
    )
}

private fun String.toStringList(): List<String> = runCatching {
    val array = JSONArray(this)
    buildList {
        for (index in 0 until array.length()) {
            array.optString(index).takeIf(String::isNotBlank)?.let(::add)
        }
    }
}.getOrDefault(emptyList())

private fun List<String>.toJsonArray(): String = JSONArray(this).toString()
