package com.example.unibox.di

import android.content.Context
import androidx.room.Room
import com.example.unibox.data.local.UniBoxDatabase
import com.example.unibox.data.local.UniBoxItemDao
import com.example.unibox.data.repository.UniBoxRepositoryImpl
import com.example.unibox.domain.repository.UniBoxRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UniBoxDatabase {
        return Room.databaseBuilder(
            context,
            UniBoxDatabase::class.java,
            UniBoxDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUniBoxItemDao(database: UniBoxDatabase): UniBoxItemDao {
        return database.uniBoxItemDao()
    }

    @Provides
    @Singleton
    fun provideUniBoxRepository(dao: UniBoxItemDao): UniBoxRepository {
        return UniBoxRepositoryImpl(dao)
    }
}
