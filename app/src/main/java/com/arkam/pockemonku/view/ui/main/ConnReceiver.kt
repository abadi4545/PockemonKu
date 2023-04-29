package com.arkam.pockemonku.view.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class ConnReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        connectivityReceiverListener?.onNetworkConnChanger(isConnectedOrConnecting(context!!))
    }

    //For checking internet connection
    fun isConnectedOrConnecting(context: Context): Boolean {
        val connectionManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    //For receiving the changes in network status
    interface ConnReceiverListener {
        fun onNetworkConnChanger(isConnected: Boolean)
    }

    companion object {
        var connectivityReceiverListener: ConnReceiverListener? = null
    }
}