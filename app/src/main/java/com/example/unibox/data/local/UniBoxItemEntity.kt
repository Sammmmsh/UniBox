package com.example.unibox.data.local

import androidx.room.Entity
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
    val imageUri: String? = null
)
