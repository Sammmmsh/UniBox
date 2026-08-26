package com.example.unibox.data.local

import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
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
    fun migrationFrom2To3PreservesItemsAndMatchesRoomSchema() {
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
                    `title`, `description`, `category`, `timestamp`, `imageUrisJson`
                ) VALUES ('Existing item', 'Preserved during upgrade', 'ARTICLE', 1000, '[]')""".trimIndent()
            )
            database.version = 2
        }

        val database = Room.databaseBuilder(context, UniBoxDatabase::class.java, TEST_DATABASE)
            .addMigrations(UniBoxDatabase.MIGRATION_2_3)
            .allowMainThreadQueries()
            .build()

        try {
            val upgradedDatabase = database.openHelper.writableDatabase

            upgradedDatabase.query(
                """SELECT title, status, isFavorite, userNote, tagsJson, updatedAt
                    FROM unibox_items WHERE id = 1""".trimIndent()
            ).use { cursor ->
                check(cursor.moveToFirst())
                assertEquals("Existing item", cursor.getString(0))
                assertEquals("INBOX", cursor.getString(1))
                assertEquals(0, cursor.getInt(2))
                assertEquals("", cursor.getString(3))
                assertEquals("[]", cursor.getString(4))
                assertEquals(0L, cursor.getLong(5))
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

    private companion object {
        const val TEST_DATABASE = "unibox-migration-test"
    }
}
