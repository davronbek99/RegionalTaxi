package dev.davron.regionaltaxi.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetworkingBroadcastReceiver(private var updateUI: UpdateUI) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context?.let { isOnline(it) }!!) {
            updateUI.isOnLine(true)
        } else {
            updateUI.isOnLine(false)
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectionManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activityNetworkInfo = connectionManager.activeNetworkInfo

        return activityNetworkInfo != null && activityNetworkInfo.isConnected
    }
}