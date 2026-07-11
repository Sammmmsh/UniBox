package com.example.unibox.presentation.share

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.unibox.presentation.theme.UniBoxTheme
import dagger.hilt.android.AndroidEntryPoint

// Activity that handles incoming share intents from other apps.
// Registered as a share target in AndroidManifest.xml for:
// - text/plain (URLs, text snippets)
// - image types (screenshots, photos)
//
// This activity parses the intent, extracts the shared data,
// and displays a confirmation/preview screen.
@AndroidEntryPoint
class ShareReceiverActivity : ComponentActivity() {

    private val shareViewModel: ShareViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedData = parseIncomingIntent(intent)

        setContent {
            UniBoxTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShareReceiverScreen(
                        sharedData = sharedData,
                        onSave = {
                            shareViewModel.saveSharedContent(sharedData) {
                                finish()
                            }
                        },
                        onDiscard = { finish() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    /**
     * Parse the incoming intent and extract shared content.
     * Handles ACTION_SEND for single items and ACTION_SEND_MULTIPLE for batches.
     */
    private fun parseIncomingIntent(intent: Intent?): SharedData {
        if (intent == null) return SharedData()

        return when (intent.action) {
            Intent.ACTION_SEND -> {
                val type = intent.type ?: ""
                when {
                    type.startsWith("text/") -> {
                        val text = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
                        val subject = intent.getStringExtra(Intent.EXTRA_SUBJECT)
                        // Try to extract a URL from the shared text
                        val url = extractUrl(text)
                        SharedData(
                            type = SharedDataType.TEXT,
                            rawText = text,
                            url = url,
                            subject = subject,
                            sourcePackage = intent.`package`
                                ?: callingPackage
                                ?: "Unknown"
                        )
                    }
                    type.startsWith("image/") -> {
                        @Suppress("DEPRECATION")
                        val imageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                        SharedData(
                            type = SharedDataType.IMAGE,
                            imageUri = imageUri?.toString(),
                            subject = intent.getStringExtra(Intent.EXTRA_SUBJECT),
                            sourcePackage = intent.`package`
                                ?: callingPackage
                                ?: "Unknown"
                        )
                    }
                    else -> SharedData(
                        type = SharedDataType.UNKNOWN,
                        rawText = "Unsupported content type: $type"
                    )
                }
            }
            Intent.ACTION_SEND_MULTIPLE -> {
                val type = intent.type ?: ""
                if (type.startsWith("image/")) {
                    @Suppress("DEPRECATION")
                    val imageUris = intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
                    SharedData(
                        type = SharedDataType.MULTI_IMAGE,
                        rawText = "${imageUris?.size ?: 0} images shared",
                        imageUri = imageUris?.firstOrNull()?.toString(),
                        sourcePackage = intent.`package`
                            ?: callingPackage
                            ?: "Unknown"
                    )
                } else {
                    SharedData(type = SharedDataType.UNKNOWN)
                }
            }
            else -> SharedData()
        }
    }

    /**
     * Extract the first URL from a text block.
     * Apps like Twitter/Instagram often share text that contains a URL mixed with other text.
     */
    private fun extractUrl(text: String): String? {
        val urlPattern = Regex(
            "https?://\\S+",
            RegexOption.IGNORE_CASE
        )
        return urlPattern.find(text)?.value
    }
}

// Represents parsed data from a share intent.

data class SharedData(
    val type: SharedDataType = SharedDataType.UNKNOWN,
    val rawText: String = "",
    val url: String? = null,
    val imageUri: String? = null,
    val subject: String? = null,
    val sourcePackage: String = "Unknown"
)

enum class SharedDataType(val label: String) {
    TEXT("Text / Link"),
    IMAGE("Image"),
    MULTI_IMAGE("Multiple Images"),
    UNKNOWN("Unknown")
}