package com.example.unibox.data.local

import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.UniBoxItem

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
        imageUri = imageUri
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
        imageUri = imageUri
    )
}
