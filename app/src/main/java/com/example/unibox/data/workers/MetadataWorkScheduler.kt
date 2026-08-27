package com.example.unibox.data.workers

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetadataWorkScheduler @Inject constructor(
    @ApplicationContext context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    fun enqueue(itemId: Long, replaceExisting: Boolean = false) {
        val request = OneTimeWorkRequestBuilder<MetadataWorker>()
            .setInputData(workDataOf(MetadataWorker.KEY_ITEM_ID to itemId))
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                MINIMUM_BACKOFF_SECONDS,
                TimeUnit.SECONDS
            )
            .addTag(WORK_TAG)
            .build()

        workManager.enqueueUniqueWork(
            MetadataWorker.WORK_NAME_PREFIX + itemId,
            if (replaceExisting) ExistingWorkPolicy.REPLACE else ExistingWorkPolicy.KEEP,
            request
        )
    }

    private companion object {
        const val WORK_TAG = "web_preview"
        const val MINIMUM_BACKOFF_SECONDS = 30L
    }
}
