package com.example.unibox.data.repository

import com.example.unibox.data.local.UniBoxItemDao
import com.example.unibox.data.local.toDomainModel
import com.example.unibox.data.local.toEntity
import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.domain.repository.UniBoxRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UniBoxRepositoryImpl @Inject constructor(
    private val dao: UniBoxItemDao
) : UniBoxRepository {

    override fun getAllItems(): Flow<List<UniBoxItem>> {
        return dao.getAllItems().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getItemsByCategory(category: Category): Flow<List<UniBoxItem>> {
        return dao.getItemsByCategory(category.name).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun searchItems(query: String): Flow<List<UniBoxItem>> {
        // FTS4 uses prefix matching with *
        val ftsQuery = "$query*"
        return dao.searchItems(ftsQuery).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun searchItemsByCategory(query: String, category: Category): Flow<List<UniBoxItem>> {
        val ftsQuery = "$query*"
        return dao.searchItemsByCategory(ftsQuery, category.name).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getItemById(id: Long): Flow<UniBoxItem?> {
        return dao.getItemById(id).map { it?.toDomainModel() }
    }

    override suspend fun saveItem(item: UniBoxItem): Long {
        return dao.insertItem(item.toEntity())
    }

    override suspend fun updateItem(item: UniBoxItem) {
        dao.updateItem(item.toEntity())
    }

    override suspend fun deleteItem(id: Long) {
        dao.deleteItemById(id)
    }

    override fun getItemCount(): Flow<Int> {
        return dao.getItemCount()
    }
}
