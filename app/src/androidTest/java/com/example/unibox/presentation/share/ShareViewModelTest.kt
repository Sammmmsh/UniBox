package com.example.unibox.presentation.share

import androidx.lifecycle.ViewModelStore
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.unibox.data.local.UniBoxDatabase
import com.example.unibox.data.media.MediaStorage
import com.example.unibox.data.repository.UniBoxRepositoryImpl
import com.example.unibox.data.workers.MetadataWorkScheduler
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.domain.organization.OrganizationEngine
import com.example.unibox.domain.repository.UniBoxRepository
import com.example.unibox.domain.usecase.SaveItemUseCase
import com.example.unibox.ml.TextExtractor
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger

class ShareViewModelTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentation.targetContext
    private val store = ViewModelStore()
    private lateinit var database: UniBoxDatabase
    private lateinit var repository: UniBoxRepository

    @Before fun setUp() {
        database = Room.inMemoryDatabaseBuilder(context, UniBoxDatabase::class.java).build()
        repository = UniBoxRepositoryImpl(database.uniBoxItemDao(), MediaStorage(context))
    }

    @After fun tearDown() {
        instrumentation.runOnMainSync { store.clear() }
        database.close()
    }

    @Test fun repeatedTapsOnlyCommitOneItem() = runBlocking {
        val gate = CompletableDeferred<Unit>()
        val insertions = AtomicInteger()
        val slowRepository = object : UniBoxRepository by repository {
            override suspend fun saveItem(item: UniBoxItem): Long {
                insertions.incrementAndGet()
                gate.await()
                return repository.saveItem(item)
            }
        }
        val model = model(slowRepository)
        val data = SharedData(type = SharedDataType.TEXT, rawText = "A shared note")
        try {
            instrumentation.runOnMainSync {
                model.saveSharedContent(data)
                model.saveSharedContent(data)
                assertTrue(model.saveState.value.isSaving)
            }
        } finally { gate.complete(Unit) }
        withTimeout(5000) { model.saveState.first { it.savedItemId != null } }
        instrumentation.runOnMainSync { model.saveSharedContent(data) }
        assertEquals(1, insertions.get())
        assertEquals(1, repository.getAllItemsSync().size)
    }

    @Test fun failedSaveKeepsARetryableStateAndDoesNotAddAnItem() = runBlocking {
        var fail = true
        val failingRepository = object : UniBoxRepository by repository {
            override suspend fun saveItem(item: UniBoxItem): Long {
                if (fail) throw IOException("Simulated storage failure")
                return repository.saveItem(item)
            }
        }
        val model = model(failingRepository)
        val data = SharedData(type = SharedDataType.TEXT, rawText = "Do not lose this")
        instrumentation.runOnMainSync { model.saveSharedContent(data) }
        val failed = withTimeout(5000) { model.saveState.first { it.error != null } }
        assertFalse(failed.isSaving)
        assertNull(failed.savedItemId)
        assertTrue(repository.getAllItemsSync().isEmpty())
        fail = false
        instrumentation.runOnMainSync { model.saveSharedContent(data) }
        withTimeout(5000) { model.saveState.first { it.savedItemId != null } }
        assertEquals("Do not lose this", repository.getAllItemsSync().single().title)
    }

    @Test fun unreadableImageDoesNotSaveAnEmptyItem() = runBlocking {
        val model = model(repository)
        instrumentation.runOnMainSync {
            model.saveSharedContent(SharedData(type = SharedDataType.IMAGE,
                imageUris = listOf("content://unibox.missing/photo")))
        }
        withTimeout(5000) { model.saveState.first { it.error != null } }
        assertTrue(repository.getAllItemsSync().isEmpty())
    }

    @Test fun unsupportedAndEmptySharesAreNotInserted() = runBlocking {
        val model = model(repository)
        for (data in listOf(SharedData(), SharedData(type = SharedDataType.TEXT),
            SharedData(type = SharedDataType.IMAGE))) {
            instrumentation.runOnMainSync { model.saveSharedContent(data) }
            assertNotNull(model.saveState.value.error)
        }
        assertTrue(repository.getAllItemsSync().isEmpty())
    }

    private fun model(source: UniBoxRepository): ShareViewModel = ShareViewModel(
        SaveItemUseCase(source, OrganizationEngine()), TextExtractor(), MediaStorage(context),
        MetadataWorkScheduler(context), context
    ).also { store.put("share", it) }
}
