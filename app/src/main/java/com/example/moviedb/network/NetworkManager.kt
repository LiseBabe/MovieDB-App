package com.example.moviedb.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class NetworkManager(context : Context) {

    var onInternetReconnect : (() -> Unit)? = null

    private var networkCallback : ConnectivityManager.NetworkCallback

    init {
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            // network is available for use
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                hasInternet = true

                onInternetReconnect?.invoke()
            }

            // lost network connection
            override fun onLost(network: Network) {
                super.onLost(network)
                hasInternet = false
            }
        }
        networkSetup(context)
    }

    var hasInternet : Boolean = true



    fun networkSetup(context : Context) {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}