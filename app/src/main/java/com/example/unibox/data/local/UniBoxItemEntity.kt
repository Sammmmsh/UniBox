package com.example.unibox.data.local

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

/**
 * Room entity representing a saved item in the UniBox database.
 * Maps to/from the domain model [com.example.unibox.domain.model.UniBoxItem].
 */
@Entity(tableName = "unibox_items")
data class UniBoxItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val description: String = "",
    val url: String? = null,
    val thumbnailUrl: String? = null,
    val extractedText: String? = null,
    val category: String = "UNCATEGORIZED",
    val sourceApp: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val locationLabel: String? = null,
    val imageUri: String? = null,
    val imageUrisJson: String = "[]",
    val status: String = "INBOX",
    val isFavorite: Boolean = false,
    val snoozedUntil: Long? = null,
    val userNote: String = "",
    val collectionName: String? = null,
    val tagsJson: String = "[]",
    @ColumnInfo(defaultValue = "0")
    val organizationReviewed: Boolean = false,
    val enrichmentStatus: String = "NOT_REQUIRED",
    val enrichmentProvider: String? = null,
    val enrichmentError: String? = null,
    val canonicalUrl: String? = null,
    val webSiteName: String? = null,
    val webAuthor: String? = null,
    val webPublishedAt: String? = null,
    val webLanguage: String? = null,
    val webReadingTimeMinutes: Int? = null,
    val lastEnrichedAt: Long? = null,
    val updatedAt: Long = System.currentTimeMillis()
)
