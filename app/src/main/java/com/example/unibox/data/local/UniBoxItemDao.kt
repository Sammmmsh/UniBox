package com.example.unibox.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for UniBox items.
 * All queries return Flow for reactive UI updates via UDF.
 */
@Dao
interface UniBoxItemDao {

    // ── Reads (reactive) ──────────────────────────────────────────────

    @Query("SELECT * FROM unibox_items ORDER BY timestamp DESC")
    fun getAllItems(): Flow<List<UniBoxItemEntity>>

    @Query("SELECT * FROM unibox_items WHERE category = :category ORDER BY timestamp DESC")
    fun getItemsByCategory(category: String): Flow<List<UniBoxItemEntity>>

    @Query("SELECT * FROM unibox_items WHERE id = :id")
    fun getItemById(id: Long): Flow<UniBoxItemEntity?>

    @Query("SELECT * FROM unibox_items WHERE id = :id")
    suspend fun getItemByIdSync(id: Long): UniBoxItemEntity?

    // ── FTS4 Full-Text Search ─────────────────────────────────────────

    @Query("""
        SELECT unibox_items.* FROM unibox_items
        JOIN unibox_items_fts ON unibox_items.rowid = unibox_items_fts.rowid
        WHERE unibox_items_fts MATCH :query
        ORDER BY unibox_items.timestamp DESC
    """)
    fun searchItems(query: String): Flow<List<UniBoxItemEntity>>

    @Query("""
        SELECT unibox_items.* FROM unibox_items
        JOIN unibox_items_fts ON unibox_items.rowid = unibox_items_fts.rowid
        WHERE unibox_items_fts MATCH :query AND unibox_items.category = :category
        ORDER BY unibox_items.timestamp DESC
    """)
    fun searchItemsByCategory(query: String, category: String): Flow<List<UniBoxItemEntity>>

    // ── Writes ────────────────────────────────────────────────────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: UniBoxItemEntity): Long

    @Update
    suspend fun updateItem(item: UniBoxItemEntity)

    // Only update organization fields, and reject stale suggestions after another edit.
    @Query("""
        UPDATE unibox_items SET category = COALESCE(:category, category),
            tagsJson = :tagsJson, collectionName = COALESCE(:collectionName, collectionName),
            organizationReviewed = 1, updatedAt = :updatedAt
        WHERE id = :id AND category = :expectedCategory AND tagsJson = :expectedTagsJson
            AND collectionName IS :expectedCollectionName AND organizationReviewed = 0
    """)
    suspend fun applyOrganizationSuggestions(
        id: Long,
        category: String?,
        tagsJson: String,
        collectionName: String?,
        expectedCategory: String,
        expectedTagsJson: String,
        expectedCollectionName: String?,
        updatedAt: Long
    ): Int

    @Query("UPDATE unibox_items SET organizationReviewed = :reviewed WHERE id = :id")
    suspend fun setOrganizationReviewed(id: Long, reviewed: Boolean)

    // Preview jobs only own these columns, so a slow request cannot revert user edits.
    @Query("""
        UPDATE unibox_items SET
            title = CASE WHEN TRIM(title) = '' OR title = url
                THEN COALESCE(:pageTitle, title) ELSE title END,
            description = CASE WHEN TRIM(description) = '' OR description = url
                THEN COALESCE(:pageDescription, description) ELSE description END,
            thumbnailUrl = COALESCE(:imageUrl, thumbnailUrl),
            extractedText = CASE WHEN imageUri IS NULL AND imageUrisJson = '[]'
                THEN COALESCE(:pageContent, extractedText) ELSE extractedText END,
            enrichmentStatus = :previewStatus,
            enrichmentProvider = :provider,
            enrichmentError = :error,
            canonicalUrl = COALESCE(:pageUrl, canonicalUrl),
            webSiteName = COALESCE(:siteName, webSiteName),
            webAuthor = COALESCE(:author, webAuthor),
            webPublishedAt = COALESCE(:publishedAt, webPublishedAt),
            webLanguage = COALESCE(:language, webLanguage),
            webReadingTimeMinutes = COALESCE(:readingTimeMinutes, webReadingTimeMinutes),
            lastEnrichedAt = :enrichedAt
        WHERE id = :id AND url = :expectedUrl
    """)
    suspend fun applyWebPreview(
        id: Long,
        expectedUrl: String,
        pageTitle: String?,
        pageDescription: String?,
        imageUrl: String?,
        pageContent: String?,
        previewStatus: String,
        provider: String,
        error: String?,
        pageUrl: String?,
        siteName: String?,
        author: String?,
        publishedAt: String?,
        language: String?,
        readingTimeMinutes: Int?,
        enrichedAt: Long
    ): Int

    @Query("""
        UPDATE unibox_items SET enrichmentStatus = :previewStatus,
            enrichmentError = :error, lastEnrichedAt = :attemptedAt
        WHERE id = :id AND url = :expectedUrl
    """)
    suspend fun updateWebPreviewState(
        id: Long,
        expectedUrl: String,
        previewStatus: String,
        error: String?,
        attemptedAt: Long
    ): Int

    @Delete
    suspend fun deleteItem(item: UniBoxItemEntity)

    @Query("DELETE FROM unibox_items WHERE id = :id")
    suspend fun deleteItemById(id: Long)

    // ── Count ─────────────────────────────────────────────────────────

    @Query("SELECT COUNT(*) FROM unibox_items")
    fun getItemCount(): Flow<Int>

    @Query("""
        SELECT DISTINCT collectionName FROM unibox_items
        WHERE collectionName IS NOT NULL AND TRIM(collectionName) != ''
        ORDER BY collectionName COLLATE NOCASE
    """)
    fun getCollectionNames(): Flow<List<String>>

    // ── Bulk operations (Settings screen) ────────────────────────────

    @Query("SELECT * FROM unibox_items ORDER BY timestamp DESC")
    suspend fun getAllItemsSync(): List<UniBoxItemEntity>

    @Query("DELETE FROM unibox_items")
    suspend fun deleteAllItems()
}
