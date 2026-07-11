package com.example.unibox.location

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages geofences for saved UniBox items.
 * When a user attaches a location to a saved item, this creates a geofence
 * that triggers a notification when they physically walk near that location.
 */
@Singleton
class GeofenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val geofencingClient: GeofencingClient =
        LocationServices.getGeofencingClient(context)

    companion object {
        const val GEOFENCE_RADIUS_METERS = 200f
        const val GEOFENCE_EXPIRATION_MS = 24 * 60 * 60 * 1000L // 24 hours
        const val ACTION_GEOFENCE_EVENT = "com.example.unibox.ACTION_GEOFENCE_EVENT"
        const val EXTRA_ITEM_ID = "extra_item_id"
        const val EXTRA_ITEM_TITLE = "extra_item_title"
    }

    /**
     * Register a geofence for a saved item location.
     */
    fun addGeofence(
        itemId: Long,
        itemTitle: String,
        latitude: Double,
        longitude: Double,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onFailure(SecurityException("Location permission not granted"))
            return
        }

        val geofence = Geofence.Builder()
            .setRequestId("unibox_item_$itemId")
            .setCircularRegion(latitude, longitude, GEOFENCE_RADIUS_METERS)
            .setExpirationDuration(GEOFENCE_EXPIRATION_MS)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        val request = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        val pendingIntent = createGeofencePendingIntent(itemId, itemTitle)

        geofencingClient.addGeofences(request, pendingIntent)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    /**
     * Remove a geofence by item ID.
     */
    fun removeGeofence(itemId: Long) {
        geofencingClient.removeGeofences(listOf("unibox_item_$itemId"))
    }

    private fun createGeofencePendingIntent(itemId: Long, itemTitle: String): PendingIntent {
        val intent = Intent(ACTION_GEOFENCE_EVENT).apply {
            setPackage(context.packageName)
            putExtra(EXTRA_ITEM_ID, itemId)
            putExtra(EXTRA_ITEM_TITLE, itemTitle)
        }
        return PendingIntent.getBroadcast(
            context,
            itemId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }
}
