package com.example.unibox.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.example.unibox.domain.repository.WebPreviewPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class WebPreviewPreferencesImpl @Inject constructor(
    context: Context
) : WebPreviewPreferences {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val _firecrawlEnabled = MutableStateFlow(
        sharedPreferences.getBoolean(FIRECRAWL_ENABLED_KEY, false)
    )
    private val _hasFirecrawlApiKey = MutableStateFlow(sharedPreferences.contains(API_KEY_DATA))

    override val firecrawlEnabled: Flow<Boolean> = _firecrawlEnabled.asStateFlow()
    override val hasFirecrawlApiKey: Flow<Boolean> = _hasFirecrawlApiKey.asStateFlow()

    override suspend fun isFirecrawlEnabled(): Boolean = _firecrawlEnabled.value

    override suspend fun setFirecrawlEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(FIRECRAWL_ENABLED_KEY, enabled).apply()
        _firecrawlEnabled.value = enabled
    }

    override suspend fun getFirecrawlApiKey(): String? = withContext(Dispatchers.IO) {
        val encrypted = sharedPreferences.getString(API_KEY_DATA, null)
            ?: return@withContext null
        runCatching {
            val parts = encrypted.split('.', limit = 2)
            require(parts.size == 2)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(
                Cipher.DECRYPT_MODE,
                getEncryptionKey(),
                GCMParameterSpec(128, Base64.decode(parts[0], Base64.NO_WRAP))
            )
            String(cipher.doFinal(Base64.decode(parts[1], Base64.NO_WRAP)), Charsets.UTF_8)
        }.getOrNull()
    }

    override suspend fun setFirecrawlApiKey(apiKey: String?) = withContext(Dispatchers.IO) {
        val normalized = apiKey?.trim()?.takeIf(String::isNotBlank)
        val editor = sharedPreferences.edit()
        if (normalized == null) {
            editor.remove(API_KEY_DATA)
        } else {
            require(normalized.length <= 256 && normalized.none(Char::isWhitespace))
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getEncryptionKey())
            val encrypted = cipher.doFinal(normalized.toByteArray(Charsets.UTF_8))
            val encoded = Base64.encodeToString(cipher.iv, Base64.NO_WRAP) + "." +
                Base64.encodeToString(encrypted, Base64.NO_WRAP)
            editor.putString(API_KEY_DATA, encoded)
        }
        check(editor.commit()) { "The key could not be saved on this device" }
        _hasFirecrawlApiKey.value = normalized != null
    }

    @Synchronized
    private fun getEncryptionKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }
        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            .apply {
                init(
                    KeyGenParameterSpec.Builder(
                        KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .build()
                )
            }.generateKey()
    }

    private companion object {
        const val PREFERENCES_NAME = "unibox_web_preview_prefs"
        const val FIRECRAWL_ENABLED_KEY = "firecrawl_enabled"
        const val API_KEY_DATA = "encrypted_firecrawl_api_key"
        const val KEY_ALIAS = "unibox.firecrawl.key.v1"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
    }
}
