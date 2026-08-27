package com.example.unibox.data.local

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.unibox.data.media.MediaStorage
import com.example.unibox.data.repository.UniBoxRepositoryImpl
import com.example.unibox.domain.model.Category
import com.example.unibox.domain.organization.OrganizationSelection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OrganizationPersistenceTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var database: UniBoxDatabase
    private lateinit var dao: UniBoxItemDao
    private lateinit var repository: UniBoxRepositoryImpl

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(context, UniBoxDatabase::class.java).build()
        dao = database.uniBoxItemDao()
        repository = UniBoxRepositoryImpl(dao, MediaStorage(context))
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun applyingSuggestionsPreservesNewPreviewAndUserFields() = runBlocking {
        val id = dao.insertItem(UniBoxItemEntity(title = "Original", tagsJson = "[\"Keep\"]"))
        val snapshot = requireNotNull(dao.getItemByIdSync(id))
        dao.updateItem(snapshot.copy(
            title = "Fetched title", extractedText = "Fetched content",
            userNote = "My note", isFavorite = true, imageUri = "file:///test/photo.jpg"
        ))
        assertTrue(repository.applyOrganizationSuggestions(
            snapshot.toDomainModel(),
            OrganizationSelection(Category.TECH, listOf("Android", "android"), "App research")
        ))
        val updated = requireNotNull(repository.getItemById(id).first())
        assertEquals("Fetched title", updated.title)
        assertEquals("Fetched content", updated.extractedText)
        assertEquals("My note", updated.userNote)
        assertTrue(updated.isFavorite)
        assertEquals("file:///test/photo.jpg", updated.imageUri)
        assertEquals(Category.TECH, updated.category)
        assertEquals(listOf("Keep", "Android"), updated.tags)
        assertEquals("App research", updated.collectionName)
        assertTrue(updated.organizationReviewed)
        assertEquals(id, dao.searchItems("Android").first().single().id)
    }

    @Test
    fun staleSuggestionsCannotReplaceNewOrganizationChoices() = runBlocking {
        val id = dao.insertItem(UniBoxItemEntity(title = "An item"))
        val snapshot = requireNotNull(dao.getItemByIdSync(id))
        dao.updateItem(snapshot.copy(collectionName = "Chosen by me", tagsJson = "[\"Personal\"]"))
        assertFalse(repository.applyOrganizationSuggestions(
            snapshot.toDomainModel(),
            OrganizationSelection(Category.TECH, listOf("Android"), "App research")
        ))
        val updated = requireNotNull(repository.getItemById(id).first())
        assertEquals("Chosen by me", updated.collectionName)
        assertEquals(listOf("Personal"), updated.tags)
        assertFalse(updated.organizationReviewed)
    }

    @Test
    fun dismissedSuggestionsCanBeReviewedAgainWithoutChangingTheItem() = runBlocking {
        val id = dao.insertItem(UniBoxItemEntity(title = "Kotlin Android", userNote = "Keep"))
        repository.setOrganizationReviewed(id, true)
        assertTrue(requireNotNull(dao.getItemByIdSync(id)).toDomainModel().organizationReviewed)
        repository.setOrganizationReviewed(id, false)
        val updated = requireNotNull(dao.getItemByIdSync(id))
        assertFalse(updated.organizationReviewed)
        assertEquals("Keep", updated.userNote)
        assertEquals("[]", updated.tagsJson)
    }
}
