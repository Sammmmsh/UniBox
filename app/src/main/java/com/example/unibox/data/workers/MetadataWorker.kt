package com.example.unibox.data.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.unibox.data.local.UniBoxItemDao
import com.example.unibox.data.remote.OpenGraphParser
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Background worker that fetches OpenGraph metadata for a saved URL.
 * Triggered when a user shares a link to UniBox.
 *
 * Flow:
 * 1. Receives the item ID via input data
 * 2. Reads the item from Room
 * 3. Fetches the URL's OpenGraph tags (title, description, thumbnail)
 * 4. Updates the item in Room with the enriched metadata
 */
@HiltWorker
class MetadataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val dao: UniBoxItemDao,
    private val openGraphParser: OpenGraphParser
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val KEY_ITEM_ID = "item_id"
        const val WORK_NAME_PREFIX = "metadata_fetch_"
    }

    override suspend fun doWork(): Result {
        val itemId = inputData.getLong(KEY_ITEM_ID, -1L)
        if (itemId == -1L) return Result.failure()

        val item = dao.getItemByIdSync(itemId) ?: return Result.failure()
        val url = item.url ?: return Result.failure()

        val ogData = openGraphParser.parse(url)

        // Update the item with fetched metadata (only overwrite blank fields)
        val updatedItem = item.copy(
            title = if (item.title == item.url || item.title.isBlank())
                ogData.title ?: item.title else item.title,
            description = if (item.description.isBlank())
                ogData.description ?: "" else item.description,
            thumbnailUrl = ogData.imageUrl ?: item.thumbnailUrl
        )

        dao.updateItem(updatedItem)

        return Result.success(
            workDataOf("fetched_title" to ogData.title)
        )
    }
}
