package dev.davron.regionaltaxi.utils

import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class LocationHelperSecondary(val context: Context, val locationCallback: LocationCallback) {

    //location details
    private var currentLocation: Location? = null
    private val UPDATE_INTERVAL: Long = 500
    private val FASTER_UPDATE_INTERVAL: Long = UPDATE_INTERVAL / 2

    private var locationRequest: LocationRequest? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null


    init {
        locationSet()
        createLocationUpdates()
        requestLocationUpdate()
    }

    private fun requestLocationUpdate() {
        CommonService.setRequestingLocationUpdates(context, true)

        try {
            fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest!!, locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {

        }
    }

    private fun createLocationUpdates() {
        locationRequest = LocationRequest()
        locationRequest?.interval = UPDATE_INTERVAL
        locationRequest?.fastestInterval = FASTER_UPDATE_INTERVAL
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun locationSet() {
        //get location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }

    fun removeLocationUpdate() {
        try {
            fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
            CommonService.setRequestingLocationUpdates(context, false)
        } catch (e: SecurityException) {
            CommonService.setRequestingLocationUpdates(context, true)
        }
    }

    private fun getLastLocation() {
        try {
            fusedLocationProviderClient!!.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    currentLocation = task.result
                } else {
                    Log.d("@@@", "getLastLocation: Failed to get location...")
                }
            }
        } catch (e: SecurityException) {
            Log.d("@@@", "getLastLocation: Faild to get location...${e.message}")
        }
    }

    fun onNewLocation(lastLocation: Location) {
        Log.d("#####################", "onNewLocation: $lastLocation")
        currentLocation = lastLocation
        Common.myLocation = currentLocation
    }

}