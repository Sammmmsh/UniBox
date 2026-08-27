package com.example.unibox.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [UniBoxItemEntity::class, UniBoxItemFts::class],
    version = 5,
    exportSchema = false
)
abstract class UniBoxDatabase : RoomDatabase() {
    abstract fun uniBoxItemDao(): UniBoxItemDao

    companion object {
        const val DATABASE_NAME = "unibox_db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE unibox_items ADD COLUMN imageUrisJson TEXT NOT NULL DEFAULT '[]'"
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN status TEXT NOT NULL DEFAULT 'INBOX'")
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN snoozedUntil INTEGER")
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN userNote TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN collectionName TEXT")
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN tagsJson TEXT NOT NULL DEFAULT '[]'")
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")

                db.execSQL("DROP TRIGGER IF EXISTS room_fts_content_sync_unibox_items_fts_BEFORE_UPDATE")
                db.execSQL("DROP TRIGGER IF EXISTS room_fts_content_sync_unibox_items_fts_BEFORE_DELETE")
                db.execSQL("DROP TRIGGER IF EXISTS room_fts_content_sync_unibox_items_fts_AFTER_UPDATE")
                db.execSQL("DROP TRIGGER IF EXISTS room_fts_content_sync_unibox_items_fts_AFTER_INSERT")
                db.execSQL("DROP TABLE IF EXISTS unibox_items_fts")
                db.execSQL(
                    """CREATE VIRTUAL TABLE IF NOT EXISTS `unibox_items_fts` USING FTS4(
                        `title` TEXT NOT NULL,
                        `description` TEXT NOT NULL,
                        `extractedText` TEXT,
                        `url` TEXT,
                        `sourceApp` TEXT,
                        `category` TEXT NOT NULL,
                        `userNote` TEXT NOT NULL,
                        `collectionName` TEXT,
                        `tagsJson` TEXT NOT NULL,
                        content=`unibox_items`
                    )""".trimIndent()
                )
                db.execSQL(
                    """CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_unibox_items_fts_BEFORE_UPDATE
                        BEFORE UPDATE ON unibox_items BEGIN
                        DELETE FROM unibox_items_fts WHERE docid=OLD.rowid;
                        END""".trimIndent()
                )
                db.execSQL(
                    """CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_unibox_items_fts_BEFORE_DELETE
                        BEFORE DELETE ON unibox_items BEGIN
                        DELETE FROM unibox_items_fts WHERE docid=OLD.rowid;
                        END""".trimIndent()
                )
                db.execSQL(
                    """CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_unibox_items_fts_AFTER_UPDATE
                        AFTER UPDATE ON unibox_items BEGIN
                        INSERT INTO unibox_items_fts(
                            docid, title, description, extractedText, url, sourceApp,
                            category, userNote, collectionName, tagsJson
                        ) VALUES (
                            NEW.rowid, NEW.title, NEW.description, NEW.extractedText,
                            NEW.url, NEW.sourceApp, NEW.category, NEW.userNote,
                            NEW.collectionName, NEW.tagsJson
                        ); END""".trimIndent()
                )
                db.execSQL(
                    """CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_unibox_items_fts_AFTER_INSERT
                        AFTER INSERT ON unibox_items BEGIN
                        INSERT INTO unibox_items_fts(
                            docid, title, description, extractedText, url, sourceApp,
                            category, userNote, collectionName, tagsJson
                        ) VALUES (
                            NEW.rowid, NEW.title, NEW.description, NEW.extractedText,
                            NEW.url, NEW.sourceApp, NEW.category, NEW.userNote,
                            NEW.collectionName, NEW.tagsJson
                        ); END""".trimIndent()
                )
                db.execSQL("INSERT INTO unibox_items_fts(unibox_items_fts) VALUES('rebuild')")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE unibox_items ADD COLUMN organizationReviewed INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE unibox_items ADD COLUMN enrichmentStatus TEXT NOT NULL DEFAULT 'NOT_REQUIRED'"
                )
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN enrichmentProvider TEXT")
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN enrichmentError TEXT")
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN canonicalUrl TEXT")
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN webSiteName TEXT")
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN webAuthor TEXT")
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN webPublishedAt TEXT")
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN webLanguage TEXT")
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN webReadingTimeMinutes INTEGER")
                db.execSQL("ALTER TABLE unibox_items ADD COLUMN lastEnrichedAt INTEGER")
                db.execSQL(
                    "UPDATE unibox_items SET enrichmentStatus = 'PARTIAL' WHERE url IS NOT NULL"
                )
            }
        }
    }
}
