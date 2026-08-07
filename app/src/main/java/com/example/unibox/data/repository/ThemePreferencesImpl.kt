package com.example.unibox.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.unibox.domain.model.ThemeMode
import com.example.unibox.domain.repository.ThemePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ThemePreferencesImpl @Inject constructor(
    private val context: Context
) : ThemePreferences {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("unibox_theme_prefs", Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(getThemeModeFromPrefs())
    override val themeMode: Flow<ThemeMode> = _themeMode.asStateFlow()

    private val preferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == THEME_MODE_KEY) {
                _themeMode.value = getThemeModeFromPrefs()
            }
        }

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override suspend fun saveThemeMode(mode: ThemeMode) {
        sharedPreferences.edit().putString(THEME_MODE_KEY, mode.name).apply()
    }

    private fun getThemeModeFromPrefs(): ThemeMode {
        val modeStr = sharedPreferences.getString(THEME_MODE_KEY, ThemeMode.SYSTEM.name)
        return try {
            ThemeMode.valueOf(modeStr ?: ThemeMode.SYSTEM.name)
        } catch (e: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
    }

    companion object {
        private const val THEME_MODE_KEY = "theme_mode"
    }
}
