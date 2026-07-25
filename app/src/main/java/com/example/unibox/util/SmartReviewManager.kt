package com.example.unibox.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages In-App Review prompts using Google Play's native API.
 * UX fix #1: Never prompt on first launch. Trigger on a success moment,
 * not a timer. Gate it: 5+ saves, 3+ sessions, never reviewed before.
 *
 * Uses the native OS review flow (rate-limited by Google Play),
 * NOT a custom modal dialog.
 */
@Singleton
class SmartReviewManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("unibox_review", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_SAVE_COUNT = "save_count"
        private const val KEY_SESSION_COUNT = "session_count"
        private const val KEY_HAS_REVIEWED = "has_reviewed"

        private const val MIN_SAVES = 5
        private const val MIN_SESSIONS = 3
    }

    fun incrementSaveCount() {
        val current = prefs.getInt(KEY_SAVE_COUNT, 0)
        prefs.edit().putInt(KEY_SAVE_COUNT, current + 1).apply()
    }

    fun incrementSessionCount() {
        val current = prefs.getInt(KEY_SESSION_COUNT, 0)
        prefs.edit().putInt(KEY_SESSION_COUNT, current + 1).apply()
    }

    fun shouldRequestReview(): Boolean {
        val saveCount = prefs.getInt(KEY_SAVE_COUNT, 0)
        val sessionCount = prefs.getInt(KEY_SESSION_COUNT, 0)
        val hasReviewed = prefs.getBoolean(KEY_HAS_REVIEWED, false)
        return saveCount >= MIN_SAVES && sessionCount >= MIN_SESSIONS && !hasReviewed
    }

    /**
     * Launches the native Google Play In-App Review flow.
     * This is OS-managed and rate-limited — Google decides whether
     * to actually show the dialog. We never show a custom modal.
     */
    fun requestReview(activity: Activity) {
        try {
            val manager = ReviewManagerFactory.create(activity)
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo = task.result
                    val flow = manager.launchReviewFlow(activity, reviewInfo)
                    flow.addOnCompleteListener {
                        // Mark as reviewed regardless of outcome
                        // (Google may not show the dialog, and that's fine)
                        prefs.edit().putBoolean(KEY_HAS_REVIEWED, true).apply()
                    }
                }
                // If it fails, silently ignore. Never annoy the user.
            }
        } catch (_: Exception) {
            // Play Services not available or other issue — do nothing
        }
    }
}
