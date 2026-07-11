package com.example.unibox.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.unibox.MainActivity
import com.example.unibox.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

/**
 * Receives geofence transition broadcasts and shows a local notification
 * when the user enters a geofenced area attached to a saved item.
 */
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "unibox_geofence"
        const val CHANNEL_NAME = "Location Reminders"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != GeofenceManager.ACTION_GEOFENCE_EVENT) return

        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return
        if (geofencingEvent.hasError()) return

        if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val itemId = intent.getLongExtra(GeofenceManager.EXTRA_ITEM_ID, -1L)
            val itemTitle = intent.getStringExtra(GeofenceManager.EXTRA_ITEM_TITLE)
                ?: "Saved item"

            showNotification(context, itemId, itemTitle)
        }
    }

    private fun showNotification(context: Context, itemId: Long, title: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel (required for Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications when you're near a saved location"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Tapping the notification opens the app
        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to_item", itemId)
        }
        val pendingTapIntent = PendingIntent.getActivity(
            context, itemId.toInt(), tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_map)
            .setContentTitle("📍 You're nearby!")
            .setContentText("You saved \"$title\" near this location.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("You saved \"$title\" near this location. Tap to view it in UniBox.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingTapIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(itemId.toInt(), notification)
    }
}
