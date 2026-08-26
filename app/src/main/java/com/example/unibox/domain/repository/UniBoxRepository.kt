package com.example.unibox.domain.repository

import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.UniBoxItem
import kotlinx.coroutines.flow.Flow

// Repository interface — the domain layer defines WHAT it needs,
// the data layer provides HOW.
interface UniBoxRepository {

    fun getAllItems(): Flow<List<UniBoxItem>>

    fun getItemsByCategory(category: Category): Flow<List<UniBoxItem>>

    fun searchItems(query: String): Flow<List<UniBoxItem>>

    fun searchItemsByCategory(query: String, category: Category): Flow<List<UniBoxItem>>

    fun getItemById(id: Long): Flow<UniBoxItem?>

    suspend fun saveItem(item: UniBoxItem): Long

    suspend fun updateItem(item: UniBoxItem)

    suspend fun deleteItem(id: Long)

    fun getItemCount(): Flow<Int>

    fun getCollectionNames(): Flow<List<String>>

    suspend fun getAllItemsSync(): List<UniBoxItem>

    suspend fun deleteAllItems()
}
