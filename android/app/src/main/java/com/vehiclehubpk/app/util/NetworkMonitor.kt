package com.vehiclehubpk.app.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkMonitor {
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(ConnectivityManager::class.java) ?: return true
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        val hasTransport =
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        return hasTransport && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
