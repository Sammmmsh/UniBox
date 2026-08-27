package com.example.unibox.data.media

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/** Copies the entire selection while its temporary read grants are valid, or rolls it back. */
@Singleton
class MediaStorage @Inject constructor(
    @ApplicationContext context: Context
) {
    private val resolver = context.contentResolver
    private val mediaDirectory = File(context.filesDir, MEDIA_DIRECTORY)

    suspend fun importImages(sourceUris: List<String>): List<String> {
        if (sourceUris.isEmpty()) return emptyList()
        val created = mutableListOf<File>()
        try {
            return withContext(Dispatchers.IO) {
                if (!mediaDirectory.isDirectory && !mediaDirectory.mkdirs()) {
                    throw IOException("Image storage is unavailable")
                }
                sourceUris.distinct().map { rawUri ->
                    currentCoroutineContext().ensureActive()
                    val source = Uri.parse(rawUri)
                    require(source.scheme == "content") { "Images must be shared with a content URI" }
                    val extension = extensionFor(resolver.getType(source))
                    val destination = File(mediaDirectory, "${UUID.randomUUID()}.$extension")
                    created += destination
                    val input = resolver.openInputStream(source)
                        ?: throw IOException("Unable to open shared image")
                    input.use {
                        destination.outputStream().use { output ->
                            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                            while (true) {
                                currentCoroutineContext().ensureActive()
                                val count = input.read(buffer)
                                if (count < 0) break
                                output.write(buffer, 0, count)
                            }
                        }
                    }
                    if (destination.length() == 0L) throw IOException("The shared image is empty")
                    Uri.fromFile(destination).toString()
                }
            }
        } catch (error: Exception) {
            // Also covers cancellation while switching back from the IO dispatcher.
            withContext(NonCancellable + Dispatchers.IO) { created.forEach { it.delete() } }
            throw error
        }
    }

    suspend fun deleteImages(localUris: List<String>) = withContext(Dispatchers.IO) {
        val ownedDirectory = mediaDirectory.canonicalFile
        localUris.distinct().forEach { rawUri ->
            val uri = Uri.parse(rawUri)
            if (uri.scheme == "file") {
                val file = uri.path?.let(::File)?.canonicalFile
                // A stale or malformed record must never delete files outside our media directory.
                if (file != null && file.parentFile == ownedDirectory && file.isFile) file.delete()
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
