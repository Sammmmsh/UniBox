package com.example.unibox.data.media

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Imports shared media into app-owned storage while the temporary grant from
 * the source app is still valid. Stored items therefore remain readable after
 * the sharing activity closes or the source app clears its cache.
 */
@Singleton
class MediaStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun importImages(sourceUris: List<String>): List<String> = withContext(Dispatchers.IO) {
        val mediaDirectory = File(context.filesDir, MEDIA_DIRECTORY).apply { mkdirs() }

        sourceUris.distinct().mapNotNull { rawUri ->
            runCatching {
                val source = Uri.parse(rawUri)
                val extension = extensionFor(context.contentResolver.getType(source))
                val destination = File(mediaDirectory, "${UUID.randomUUID()}.$extension")

                context.contentResolver.openInputStream(source).use { input ->
                    requireNotNull(input) { "Unable to open shared image" }
                    destination.outputStream().use(input::copyTo)
                }

                Uri.fromFile(destination).toString()
            }.getOrNull()
        }
    }

    suspend fun deleteImages(localUris: List<String>) = withContext(Dispatchers.IO) {
        localUris.distinct().forEach { rawUri ->
            val uri = Uri.parse(rawUri)
            if (uri.scheme == "file") {
                uri.path?.let(::File)?.takeIf(File::isFile)?.delete()
            }
        }
    }

    private fun extensionFor(mimeType: String?): String = when (mimeType) {
        "image/png" -> "png"
        "image/webp" -> "webp"
        "image/gif" -> "gif"
        "image/heic", "image/heif" -> "heic"
        else -> "jpg"
    }

    private companion object {
        const val MEDIA_DIRECTORY = "saved-media"
    }
}
