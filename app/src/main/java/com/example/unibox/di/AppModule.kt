package com.example.unibox.di

import android.content.Context
import androidx.room.Room
import com.example.unibox.data.local.UniBoxDatabase
import com.example.unibox.data.local.UniBoxItemDao
import com.example.unibox.data.media.MediaStorage
import com.example.unibox.data.remote.PublicNetworkDns
import com.example.unibox.data.repository.UniBoxRepositoryImpl
import com.example.unibox.data.repository.WebPreviewPreferencesImpl
import com.example.unibox.domain.repository.UniBoxRepository
import com.example.unibox.domain.repository.WebPreviewPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .dns(PublicNetworkDns())
            .followRedirects(false)
            .followSslRedirects(false)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(40, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UniBoxDatabase {
        return Room.databaseBuilder(
            context,
            UniBoxDatabase::class.java,
            UniBoxDatabase.DATABASE_NAME
        )
            .addMigrations(
                UniBoxDatabase.MIGRATION_1_2,
                UniBoxDatabase.MIGRATION_2_3,
                UniBoxDatabase.MIGRATION_3_4,
                UniBoxDatabase.MIGRATION_4_5
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideUniBoxItemDao(database: UniBoxDatabase): UniBoxItemDao {
        return database.uniBoxItemDao()
    }

    @Provides
    @Singleton
    fun provideUniBoxRepository(
        dao: UniBoxItemDao,
        mediaStorage: MediaStorage
    ): UniBoxRepository {
        return UniBoxRepositoryImpl(dao, mediaStorage)
    }

    @Provides
    @Singleton
    fun provideThemePreferences(@ApplicationContext context: Context): com.example.unibox.domain.repository.ThemePreferences {
        return com.example.unibox.data.repository.ThemePreferencesImpl(context)
    }

    @Provides
    @Singleton
    fun provideWebPreviewPreferences(
        @ApplicationContext context: Context
    ): WebPreviewPreferences = WebPreviewPreferencesImpl(context)
}
