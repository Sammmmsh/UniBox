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

    @Delete
    suspend fun deleteItem(item: UniBoxItemEntity)

    @Query("DELETE FROM unibox_items WHERE id = :id")
    suspend fun deleteItemById(id: Long)

    // ── Count ─────────────────────────────────────────────────────────

    @Query("SELECT COUNT(*) FROM unibox_items")
    fun getItemCount(): Flow<Int>
}
