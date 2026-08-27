package com.example.unibox.domain.repository

import kotlinx.coroutines.flow.Flow

interface WebPreviewPreferences {
    val firecrawlEnabled: Flow<Boolean>
    val hasFirecrawlApiKey: Flow<Boolean>
    suspend fun isFirecrawlEnabled(): Boolean
    suspend fun setFirecrawlEnabled(enabled: Boolean)
    suspend fun getFirecrawlApiKey(): String?
    suspend fun setFirecrawlApiKey(apiKey: String?)
}
