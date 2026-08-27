package com.example.unibox.data.repository

import com.example.unibox.data.local.UniBoxItemDao
import com.example.unibox.data.local.UniBoxItemEntity
import com.example.unibox.data.local.toDomainModel
import com.example.unibox.data.local.toEntity
import com.example.unibox.data.local.librarySearchQuery
import com.example.unibox.data.media.MediaStorage
import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.domain.organization.OrganizationSelection
import com.example.unibox.domain.repository.UniBoxRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOf
import org.json.JSONArray
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UniBoxRepositoryImpl @Inject constructor(
    private val dao: UniBoxItemDao,
    private val mediaStorage: MediaStorage
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
        val ftsQuery = librarySearchQuery(query) ?: return flowOf(emptyList())
        return dao.searchItems(ftsQuery).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun searchItemsByCategory(query: String, category: Category): Flow<List<UniBoxItem>> {
        val ftsQuery = librarySearchQuery(query) ?: return flowOf(emptyList())
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

    override suspend fun setFavorite(id: Long, favorite: Boolean): Boolean =
        dao.setFavorite(id, favorite, System.currentTimeMillis()) > 0

    override suspend fun saveToLibrary(id: Long): Boolean =
        dao.saveToLibrary(id, System.currentTimeMillis()) > 0

    override suspend fun applyOrganizationSuggestions(
        expectedItem: UniBoxItem,
        selection: OrganizationSelection
    ): Boolean {
        if (selection.isEmpty) return false
        val tags = (expectedItem.tags + selection.tags)
            .map(String::trim)
            .filter(String::isNotBlank)
            .distinctBy { it.lowercase(Locale.ROOT) }
        return dao.applyOrganizationSuggestions(
            id = expectedItem.id,
            category = selection.category?.name,
            tagsJson = JSONArray(tags).toString(),
            collectionName = selection.collectionName,
            expectedCategory = expectedItem.category.name,
            expectedTagsJson = JSONArray(expectedItem.tags).toString(),
            expectedCollectionName = expectedItem.collectionName,
            updatedAt = System.currentTimeMillis()
        ) > 0
    }

    override suspend fun setOrganizationReviewed(id: Long, reviewed: Boolean) {
        dao.setOrganizationReviewed(id, reviewed)
    }

    override suspend fun deleteItem(id: Long) {
        dao.getItemByIdSync(id)?.toDomainModel()?.let { item ->
            mediaStorage.deleteImages(item.imageUris + listOfNotNull(item.imageUri))
        }
        dao.deleteItemById(id)
    }

    override fun getItemCount(): Flow<Int> {
        return dao.getItemCount()
    }

    override fun getCollectionNames(): Flow<List<String>> {
        return dao.getCollectionNames()
    }

    override suspend fun getAllItemsSync(): List<UniBoxItem> {
        return dao.getAllItemsSync().map { it.toDomainModel() }
    }

    override suspend fun deleteAllItems() {
        dao.getAllItemsSync().map(UniBoxItemEntity::toDomainModel).forEach { item ->
            mediaStorage.deleteImages(item.imageUris + listOfNotNull(item.imageUri))
        }
        dao.deleteAllItems()
    }
}
