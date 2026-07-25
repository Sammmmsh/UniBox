package com.example.unibox.ml

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wrapper around Google ML Kit Text Recognition.
 * Extracts text from images/screenshots so users can search for content
 * that was only visible as pixels (e.g., an Instagram restaurant post screenshot).
 */
@Singleton
class TextExtractor @Inject constructor() {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    /**
     * Extract text from an image URI.
     * Returns the full extracted text, or null if extraction fails or yields nothing.
     */
    suspend fun extractFromUri(context: Context, imageUri: Uri): String? {
        return try {
            val image = InputImage.fromFilePath(context, imageUri)
            val result = recognizer.process(image). await()
            val text = result.text.trim()
            text.ifBlank { null }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Extract text and return structured blocks (useful for future features
     * like detecting addresses, phone numbers, etc.)
     */
    suspend fun extractBlocksFromUri(context: Context, imageUri: Uri): List<String> {
        return try {
            val image = InputImage.fromFilePath(context, imageUri)
            val result = recognizer.process(image).await()
            result.textBlocks.map { it.text }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
