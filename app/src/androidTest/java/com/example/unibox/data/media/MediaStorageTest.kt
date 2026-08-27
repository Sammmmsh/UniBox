package com.example.unibox.data.media

import android.net.Uri
import androidx.core.content.FileProvider
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.util.UUID

class MediaStorageTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val storage = MediaStorage(context)
    private val ownedDirectory get() = File(context.filesDir, "saved-media")

    @Test fun imagesAreCopiedOnceAndRemainAfterTheSourceIsRemoved() = runBlocking {
        val source = fixture(byteArrayOf(1, 2, 3, 4))
        val sourceUri = uri(source)
        var imported = emptyList<String>()
        try {
            imported = storage.importImages(listOf(sourceUri, sourceUri))
            assertEquals(1, imported.size)
            source.delete()
            val saved = File(requireNotNull(Uri.parse(imported.single()).path))
            assertEquals(ownedDirectory.canonicalFile, saved.canonicalFile.parentFile)
            assertArrayEquals(byteArrayOf(1, 2, 3, 4), saved.readBytes())
        } finally {
            storage.deleteImages(imported)
            source.delete()
        }
    }

    @Test fun aFailedBatchRemovesAllOfItsPartialCopies() = runBlocking {
        val source = fixture(byteArrayOf(1, 2))
        val before = ownedDirectory.list()?.toSet().orEmpty()
        try {
            val result = runCatching {
                storage.importImages(listOf(uri(source), "content://unibox.missing/image"))
            }
            assertTrue(result.isFailure)
            assertEquals(before, ownedDirectory.list()?.toSet().orEmpty())
            assertTrue(source.exists())
        } finally { source.delete() }
    }

    @Test fun emptyImagesAndUntrustedFileUrisCannotBeSaved() = runBlocking {
        val source = fixture(byteArrayOf())
        val before = ownedDirectory.list()?.toSet().orEmpty()
        try {
            assertTrue(runCatching { storage.importImages(listOf(uri(source))) }.isFailure)
            assertTrue(runCatching { storage.importImages(listOf(Uri.fromFile(source).toString())) }.isFailure)
            assertEquals(before, ownedDirectory.list()?.toSet().orEmpty())
        } finally { source.delete() }
    }

    @Test fun deletionCannotEscapeTheOwnedMediaDirectory() = runBlocking {
        val source = fixture(byteArrayOf(9))
        try {
            storage.deleteImages(listOf(Uri.fromFile(source).toString()))
            assertTrue(source.exists())
        } finally { source.delete() }
    }

    private fun fixture(bytes: ByteArray): File {
        val directory = File(context.cacheDir, "media-tests").apply { mkdirs() }
        return File(directory, "${UUID.randomUUID()}.png").apply { writeBytes(bytes) }
    }

    private fun uri(file: File): String = FileProvider.getUriForFile(
        context, context.packageName + ".test-media", file
    ).toString()
}
