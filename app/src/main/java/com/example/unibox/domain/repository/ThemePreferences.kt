package com.example.unibox.domain.repository

import com.example.unibox.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface ThemePreferences {
    val themeMode: Flow<ThemeMode>
    suspend fun saveThemeMode(mode: ThemeMode)
}
