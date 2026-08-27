package com.example.unibox.data.local

import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UniBoxDatabaseMigrationTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        context.deleteDatabase(TEST_DATABASE)
    }

    @After
    fun tearDown() {
        context.deleteDatabase(TEST_DATABASE)
    }

    @Test
    fun migrationFrom2To4PreservesItemsAndMatchesRoomSchema() {
        val databaseFile = context.getDatabasePath(TEST_DATABASE)
        databaseFile.parentFile?.mkdirs()

        SQLiteDatabase.openOrCreateDatabase(databaseFile, null).use { database ->
            database.execSQL(
                """CREATE TABLE `unibox_items` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `title` TEXT NOT NULL,
                    `description` TEXT NOT NULL,
                    `url` TEXT,
                    `thumbnailUrl` TEXT,
                    `extractedText` TEXT,
                    `category` TEXT NOT NULL,
                    `sourceApp` TEXT,
                    `timestamp` INTEGER NOT NULL,
                    `latitude` REAL,
                    `longitude` REAL,
                    `locationLabel` TEXT,
                    `imageUri` TEXT,
                    `imageUrisJson` TEXT NOT NULL DEFAULT '[]'
                )""".trimIndent()
            )
            database.execSQL(
                """CREATE VIRTUAL TABLE `unibox_items_fts` USING FTS4(
                    `title` TEXT NOT NULL,
                    `description` TEXT NOT NULL,
                    `extractedText` TEXT,
                    `url` TEXT,
                    `sourceApp` TEXT,
                    `category` TEXT NOT NULL,
                    content=`unibox_items`
                )""".trimIndent()
            )
            database.execSQL(
                """INSERT INTO `unibox_items`(
                    `title`, `description`, `url`, `category`, `timestamp`, `imageUrisJson`
                ) VALUES (
                    'Existing item',
                    'Preserved during upgrade',
                    'https://example.com/article',
                    'ARTICLE',
                    1000,
                    '[]'
                )""".trimIndent()
            )
            database.version = 2
        }

        val database = Room.databaseBuilder(context, UniBoxDatabase::class.java, TEST_DATABASE)
            .addMigrations(
                UniBoxDatabase.MIGRATION_2_3,
                UniBoxDatabase.MIGRATION_3_4
            )
            .allowMainThreadQueries()
            .build()

        try {
            val upgradedDatabase = database.openHelper.writableDatabase

            upgradedDatabase.query(
                """SELECT title, status, isFavorite, userNote, tagsJson, updatedAt,
                    enrichmentStatus
                    FROM unibox_items WHERE id = 1""".trimIndent()
            ).use { cursor ->
                check(cursor.moveToFirst())
                assertEquals("Existing item", cursor.getString(0))
                assertEquals("INBOX", cursor.getString(1))
                assertEquals(0, cursor.getInt(2))
                assertEquals("", cursor.getString(3))
                assertEquals("[]", cursor.getString(4))
                assertEquals(0L, cursor.getLong(5))
                assertEquals("PARTIAL", cursor.getString(6))
            }

            upgradedDatabase.query(
                """SELECT COUNT(*) FROM unibox_items_fts
                    WHERE unibox_items_fts MATCH 'Existing'""".trimIndent()
            ).use { cursor ->
                check(cursor.moveToFirst())
                assertEquals(1, cursor.getInt(0))
            }
        } finally {
            database.close()
        }
    }

    @Test
    fun previewWritesPreserveUserEditsAndIndexUrlOnlyContent() = runBlocking {
        val database = Room.inMemoryDatabaseBuilder(context, UniBoxDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        try {
            val dao = database.uniBoxItemDao()
            val url = "https://example.com/article"
            val imageItemId = dao.insertItem(
                UniBoxItemEntity(
                    title = "My edited title",
                    description = "My description",
                    url = url,
                    userNote = "Keep this note",
                    isFavorite = true,
                    status = "SAVED",
                    imageUri = "file:///test/image.jpg",
                    extractedText = "Original OCR"
                )
            )
            assertEquals(1, dao.applyTestPreview(imageItemId, url))
            val imageItem = requireNotNull(dao.getItemByIdSync(imageItemId))
            assertEquals("My edited title", imageItem.title)
            assertEquals("My description", imageItem.description)
            assertEquals("Keep this note", imageItem.userNote)
            assertEquals(true, imageItem.isFavorite)
            assertEquals("SAVED", imageItem.status)
            assertEquals("Original OCR", imageItem.extractedText)

            val linkItemId = dao.insertItem(UniBoxItemEntity(title = url, url = url))
            assertEquals(1, dao.applyTestPreview(linkItemId, url))
            val linkItem = requireNotNull(dao.getItemByIdSync(linkItemId))
            assertEquals("Fetched title", linkItem.title)
            assertEquals("Indexed body text", linkItem.extractedText)
            assertEquals(0, dao.applyTestPreview(linkItemId, "https://example.com/stale"))

            database.openHelper.readableDatabase.query(
                "SELECT COUNT(*) FROM unibox_items_fts WHERE unibox_items_fts MATCH 'Indexed'"
            ).use { cursor ->
                check(cursor.moveToFirst())
                assertEquals(1, cursor.getInt(0))
            }
        } finally {
            database.close()
        }
    }

    private suspend fun UniBoxItemDao.applyTestPreview(id: Long, url: String): Int =
        applyWebPreview(
            id = id,
            expectedUrl = url,
            pageTitle = "Fetched title",
            pageDescription = "Fetched description",
            imageUrl = null,
            pageContent = "Indexed body text",
            previewStatus = "COMPLETE",
            provider = "Firecrawl",
            error = null,
            pageUrl = url,
            siteName = "Example",
            author = null,
            publishedAt = null,
            language = "en",
            readingTimeMinutes = 1,
            enrichedAt = 2000
        )

    private companion object {
        const val TEST_DATABASE = "unibox-migration-test"
    }
}
