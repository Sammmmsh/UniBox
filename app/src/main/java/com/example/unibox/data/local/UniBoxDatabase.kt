package com.example.unibox.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [UniBoxItemEntity::class, UniBoxItemFts::class],
    version = 2,
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
    }
}
