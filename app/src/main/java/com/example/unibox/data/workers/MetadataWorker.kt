package com.example.unibox.data.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.unibox.data.local.UniBoxItemDao
import com.example.unibox.data.remote.WebContentEnricher
import com.example.unibox.data.remote.WebEnrichmentResult
import com.example.unibox.data.remote.decision
import com.example.unibox.data.remote.toReadableText
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Background worker that builds a searchable preview for a saved URL.
 * Triggered when a user shares a link to UniBox.
 *
 * Flow:
 * 1. Receives the item ID via input data
 * 2. Reads the item from Room
 * 3. Uses the opted-in Firecrawl pipeline or direct page metadata fallback
 * 4. Updates the item in Room with the enriched metadata
 */
@HiltWorker
class MetadataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val dao: UniBoxItemDao,
    private val webContentEnricher: WebContentEnricher
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val KEY_ITEM_ID = "item_id"
        const val WORK_NAME_PREFIX = "metadata_fetch_"
        private const val MAX_ERROR_LENGTH = 300
    }

    override suspend fun doWork(): Result {
        val itemId = inputData.getLong(KEY_ITEM_ID, -1L)
        if (itemId == -1L) return Result.failure()

        val item = dao.getItemByIdSync(itemId) ?: return Result.failure()
        val url = item.url ?: return Result.failure()

        val enrichment = webContentEnricher.enrich(url)
        val decision = enrichment.decision(runAttemptCount)
        return when (enrichment) {
            is WebEnrichmentResult.Success -> {
                val page = enrichment.page
                val rowsUpdated = dao.applyWebPreview(
                    id = itemId,
                    expectedUrl = url,
                    pageTitle = page.title,
                    pageDescription = page.description,
                    imageUrl = page.imageUrl,
                    pageContent = page.markdown?.toReadableText(),
                    previewStatus = decision.status.name,
                    provider = enrichment.provider,
                    error = enrichment.warning?.take(MAX_ERROR_LENGTH),
                    pageUrl = page.canonicalUrl,
                    siteName = page.siteName,
                    author = page.author,
                    publishedAt = page.publishedAt,
                    language = page.language,
                    readingTimeMinutes = page.readingTimeMinutes,
                    enrichedAt = System.currentTimeMillis()
                )

                if (decision.retry && rowsUpdated > 0) {
                    Result.retry()
                } else {
                    Result.success(
                        workDataOf("preview_provider" to enrichment.provider)
                    )
                }
            }

            is WebEnrichmentResult.Failure -> {
                val rowsUpdated = dao.updateWebPreviewState(
                    id = itemId,
                    expectedUrl = url,
                    previewStatus = decision.status.name,
                    error = enrichment.message.take(MAX_ERROR_LENGTH),
                    attemptedAt = System.currentTimeMillis()
                )
                if (decision.retry && rowsUpdated > 0) Result.retry() else Result.success()
            }
        }
    }

}
