package com.example.unibox.data.local

import androidx.room.Entity
import androidx.room.Fts4

/**
 * FTS4 virtual table for full-text search across UniBox items.
 * Indexes title, description, extractedText, url, sourceApp, and category
 * so users can search across ALL saved content instantly.
 *
 * The contentEntity links this FTS table to the actual [UniBoxItemEntity] table,
 * meaning Room keeps them in sync automatically.
 */
@Fts4(contentEntity = UniBoxItemEntity::class)
@Entity(tableName = "unibox_items_fts")
data class UniBoxItemFts(
    val title: String,
    val description: String,
    val extractedText: String?,
    val url: String?,
    val sourceApp: String?,
    val category: String,
    val userNote: String,
    val collectionName: String?,
    val tagsJson: String
)
