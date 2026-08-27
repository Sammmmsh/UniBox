package com.example.unibox.data.local

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.unibox.data.media.MediaStorage
import com.example.unibox.data.repository.UniBoxRepositoryImpl
import com.example.unibox.domain.model.Category
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LibraryInteractionPersistenceTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var database: UniBoxDatabase
    private lateinit var dao: UniBoxItemDao
    private lateinit var repository: UniBoxRepositoryImpl

    @Before fun setUp() {
        database = Room.inMemoryDatabaseBuilder(context, UniBoxDatabase::class.java).build()
        dao = database.uniBoxItemDao()
        repository = UniBoxRepositoryImpl(dao, MediaStorage(context))
    }

    @After fun tearDown() = database.close()

    @Test fun punctuationAndEveryWordPrefixAreSafeOnRealFts4() = runBlocking {
        val id = dao.insertItem(UniBoxItemEntity(title = "Android layouts", description = "A Kotlin reference"))
        assertEquals(id, repository.searchItems("\"Andr\" (Kotl):").first().single().id)
        assertEquals(id, repository.searchItems("Andr lay").first().single().id)
        for (query in listOf("\"", "()", "***", ":", "-")) {
            assertTrue(repository.searchItems(query).first().isEmpty())
        }
    }

    @Test fun reservedOperatorsAreJustWords() = runBlocking {
        val id = dao.insertItem(UniBoxItemEntity(title = "OR NEAR NOT"))
        dao.insertItem(UniBoxItemEntity(title = "Android Kotlin"))
        assertEquals(id, repository.searchItems("OR").first().single().id)
        assertEquals(id, repository.searchItems("NEAR NOT").first().single().id)
    }

    @Test fun searchIncludesTagsCollectionsAndNotesAndRespectsCategory() = runBlocking {
        val id = dao.insertItem(UniBoxItemEntity(
            title = "A reference", category = "TECH", collectionName = "Build with Compose",
            tagsJson = "[\"Accessibility\"]", userNote = "Focus order"
        ))
        for (query in listOf("access", "comp", "focus")) {
            assertEquals(id, repository.searchItemsByCategory(query, Category.TECH).first().single().id)
            assertTrue(repository.searchItemsByCategory(query, Category.FOOD).first().isEmpty())
        }
    }

    @Test fun quickActionsPreserveTheLatestItemContent() = runBlocking {
        val id = dao.insertItem(UniBoxItemEntity(
            title = "Fresh preview", description = "Fetched description", extractedText = "Full content",
            userNote = "Do not lose this", tagsJson = "[\"Kotlin\"]",
            collectionName = "Work", organizationReviewed = true, imageUri = "file:///photo.jpg"
        ))
        assertTrue(repository.setFavorite(id, true))
        assertTrue(repository.saveToLibrary(id))
        val saved = requireNotNull(dao.getItemByIdSync(id))
        assertEquals("Fresh preview", saved.title)
        assertEquals("Full content", saved.extractedText)
        assertEquals("Do not lose this", saved.userNote)
        assertEquals("[\"Kotlin\"]", saved.tagsJson)
        assertEquals("Work", saved.collectionName)
        assertEquals("file:///photo.jpg", saved.imageUri)
        assertTrue(saved.organizationReviewed)
        assertTrue(saved.isFavorite)
        assertEquals("SAVED", saved.status)
        assertFalse(repository.saveToLibrary(id))
    }

    @Test fun staleSaveCannotUnarchiveAnItemOrRecreateADeletedOne() = runBlocking {
        val id = dao.insertItem(UniBoxItemEntity(title = "Archived", status = "ARCHIVED"))
        assertFalse(repository.saveToLibrary(id))
        assertEquals("ARCHIVED", dao.getItemByIdSync(id)?.status)
        dao.deleteItemById(id)
        assertFalse(repository.setFavorite(id, true))
        assertFalse(repository.saveToLibrary(id))
        assertEquals(0, dao.getItemCount().first())
    }
}
