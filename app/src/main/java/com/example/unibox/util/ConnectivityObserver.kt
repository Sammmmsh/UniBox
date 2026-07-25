package com.example.unibox.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Observes real-time network connectivity using ConnectivityManager.
 * Exposes a StateFlow<Boolean> so the UI can show an offline banner.
 * UX fix #8: "No empty state, no offline state."
 */
@Singleton
class ConnectivityObserver @Inject constructor(
    @ApplicationContext context: Context
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val isOnline: StateFlow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                caps: NetworkCapabilities
            ) {
                val hasInternet = caps.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                ) && caps.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
                trySend(hasInternet)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Check initial state
        val activeNetwork = connectivityManager.activeNetwork
        val activeCaps = connectivityManager.getNetworkCapabilities(activeNetwork)
        val initialState = activeCaps?.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        ) == true && activeCaps.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED
        )
        trySend(initialState)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = true // Assume online until proven otherwise
    )
}
