package dev.davron.regionaltaxi.utils

import android.content.Context
import android.location.Location
import android.preference.PreferenceManager
import java.text.DateFormat
import java.util.Date

object CommonService {
    val KEY_REQUEST_LOCATION_UPDATE = "location"
    fun getLocation(location: Location?): String {
        return if (location == null) {
            "Unknown location"
        } else {
            "${location.longitude} ${location.latitude}"
        }
    }

    fun getLocationTitle(context: Context): String {
        return String.format("Location update $1$", DateFormat.getDateInstance().format(Date()))
    }

    fun setRequestingLocationUpdates(context: Context, b: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(KEY_REQUEST_LOCATION_UPDATE, b)
            .apply()
    }

    fun requestingLocationUpdates(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
            KEY_REQUEST_LOCATION_UPDATE, false
        )
    }
}