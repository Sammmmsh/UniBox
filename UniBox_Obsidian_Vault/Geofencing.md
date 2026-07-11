# Geofencing (Location Reminders) 📍

UniBox can remind users about saved content when they physically visit a specific location.

## How it works

### 1. Registering the Geofence
When a user taps "Add Location Reminder" on the Detail Screen, the `GeofenceManager` uses the **Google Play Services Location API** to register a geofence.
- **Radius**: 200 meters
- **Expiration**: 24 hours
- **Trigger**: `GEOFENCE_TRANSITION_ENTER` (When the user crosses into the boundary).

A `PendingIntent` is generated and handed to the Android OS.

### 2. The Broadcast Receiver
Android continuously monitors the device's location in the background (efficiently, via cell towers and Wi-Fi networks). 
When the user enters the 200m radius, the OS fires the `PendingIntent`.

This wakes up the `GeofenceBroadcastReceiver` inside UniBox.
The receiver generates a high-priority `NotificationCompat` push notification:
> 📍 **You're nearby!** 
> You saved 'Café Luna' near this location. Tap to view it in UniBox.

When the notification is tapped, the `MainActivity` is launched with deep-link arguments to directly open that specific item.
