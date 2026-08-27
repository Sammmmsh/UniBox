package com.example.unibox.data.export

import android.net.Uri
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.unibox.data.local.UniBoxDatabase
import com.example.unibox.data.media.MediaStorage
import com.example.unibox.data.repository.UniBoxRepositoryImpl
import com.example.unibox.domain.model.ItemStatus
import com.example.unibox.domain.model.UniBoxItem
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File
import java.util.UUID

class LibraryExporterTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var database: UniBoxDatabase
    private lateinit var repository: UniBoxRepositoryImpl

    @Before fun setUp() {
        database = Room.inMemoryDatabaseBuilder(context, UniBoxDatabase::class.java).build()
        repository = UniBoxRepositoryImpl(database.uniBoxItemDao(), MediaStorage(context))
    }

    @After fun tearDown() = database.close()

    @Test fun writesAllSectionsToTheChosenFileWithoutChangingTheLibrary() = runBlocking {
        ItemStatus.entries.forEach { status ->
            repository.saveItem(UniBoxItem(title = "Caf\u00e9 \"ideas\"", status = status,
                userNote = "Line one\nLine two", collectionName = "Weekend", tags = listOf("Design"),
                isFavorite = true, organizationReviewed = true, snoozedUntil = 123456L))
        }
        val before = repository.getAllItemsSync()
        val file = File(context.cacheDir, "export-test-${UUID.randomUUID()}.json")
        try {
            assertEquals(3, LibraryExporter(repository, context).exportTo(Uri.fromFile(file)))
            val exported = JSONArray(file.readText())
            assertEquals(3, exported.length())
            val item = exported.getJSONObject(0)
            assertEquals("Caf\u00e9 \"ideas\"", item.getString("title"))
            assertEquals("Line one\nLine two", item.getString("userNote"))
            assertEquals("Design", item.getJSONArray("tags").getString(0))
            assertTrue(item.getBoolean("organizationReviewed"))
            assertTrue(item.getBoolean("isFavorite"))
            assertTrue(item.isNull("url"))
            assertEquals(before, repository.getAllItemsSync())
        } finally { file.delete() }
    }

    @Test fun emptyLibraryIsAValidJsonArray() {
        assertEquals("[]", libraryJson(emptyList()).toString())
    }

    @Test fun exportIsAnExplicitRecordOnlyAllowlist() {
        val record = libraryJson(listOf(UniBoxItem(title = "A note"))).getJSONObject(0)
        val expected = setOf(
            "id", "title", "description", "url", "thumbnailUrl", "extractedText", "category",
            "sourceApp", "timestamp", "latitude", "longitude", "locationLabel", "imageUri",
            "imageUris", "status", "isFavorite", "snoozedUntil", "userNote", "collectionName",
            "tags", "organizationReviewed", "enrichmentStatus", "enrichmentProvider",
            "enrichmentError", "canonicalUrl", "webSiteName", "webAuthor", "webPublishedAt",
            "webLanguage", "webReadingTimeMinutes", "lastEnrichedAt", "updatedAt"
        )
        assertEquals(expected, record.keys().asSequence().toSet())
    }

    @Test fun unwritableDestinationReportsFailureAndKeepsAllItems() = runBlocking {
        repository.saveItem(UniBoxItem(title = "Keep this note"))
        val before = repository.getAllItemsSync()
        val uri = Uri.parse("content://unibox.missing/export.json")
        assertTrue(runCatching { LibraryExporter(repository, context).exportTo(uri) }.isFailure)
        assertEquals(before, repository.getAllItemsSync())
    }
}
