package com.example.unibox.data.local

import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.UniBoxItem
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
        imageUris = imageUrisJson.toUriList()
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
        imageUrisJson = imageUris.toJsonArray()
    )
}

private fun String.toUriList(): List<String> = runCatching {
    val array = JSONArray(this)
    buildList {
        for (index in 0 until array.length()) {
            array.optString(index).takeIf(String::isNotBlank)?.let(::add)
        }
    }
}.getOrDefault(emptyList())

private fun List<String>.toJsonArray(): String = JSONArray(this).toString()
