package com.example.unibox.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UniBoxItemEntity::class, UniBoxItemFts::class],
    version = 1,
    exportSchema = false
)
abstract class UniBoxDatabase : RoomDatabase() {
    abstract fun uniBoxItemDao(): UniBoxItemDao

    companion object {
        const val DATABASE_NAME = "unibox_db"
    }
}
