package dev.davron.regionaltaxi.ui.fragments.taxi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dev.davron.regionaltaxi.R
import dev.davron.regionaltaxi.databinding.FragmentTaxiBinding
import dev.davron.regionaltaxi.utils.Common
import dev.davron.regionaltaxi.utils.LocationHelperSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TaxiFragment : Fragment(), OnMapReadyCallback, GoogleMap.CancelableCallback {

    private lateinit var binding: FragmentTaxiBinding
    private lateinit var currentLocation: LatLng
    private var isLocationPermissionGranted: Boolean = false
    private var locationManager: LocationManager? = null
    private var isGPSOn: Boolean = false
    private val zoomValue: Float = 18f

    private var googleMap: GoogleMap? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaxiBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initMap()
    }

    private fun init() {
        currentLocation = LatLng(Common.defLat, Common.defLng)
    }

    private fun initMap() {
        val map = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        map.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val cameraPosition = CameraPosition.builder().target(currentLocation).zoom(15f)
        googleMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(cameraPosition.build()), 900, this
        )

        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.isTiltGesturesEnabled = false
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.setMaxZoomPreference(18f)

        onUIClickListener()
        lifecycleScope.launch {
            delay(1000)
        }
    }

    private fun onUIClickListener() {
        binding.btnCurrentLocation.setOnClickListener {
            locationController()
        }
    }

    private fun locationController() {
        if (isLocationPermissionGranted) {
            Toast.makeText(requireContext(), "isLocationPermissionGranted", Toast.LENGTH_SHORT)
                .show()
            if (isGPSOn) {
                Toast.makeText(requireContext(), "isGPSOn", Toast.LENGTH_SHORT).show()
                animateMapToCurrentLocation()
            } else {
                turnOnGPS()
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        Dexter.withContext(requireContext()).withPermissions(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    if (report.areAllPermissionsGranted()) {
                        isLocationPermissionGranted = true
                        requestLocationUpdates()
                    } else {
                        openSettingsDialog()
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?, token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }
        })
            .withErrorListener {
                Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
            }.check()
    }

    private fun openSettingsDialog() {
val openSettingsBottomSheetFragment=Ope
    }

    override fun onResume() {
        super.onResume()

        //initialize location gps providers
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isGPSOn = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

        //check location permission is granted or not
        isLocationPermissionGranted =
            requireContext().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && requireContext().checkCallingOrSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        Toast.makeText(requireContext(), isLocationPermissionGranted.toString(), Toast.LENGTH_SHORT)
            .show()
        if (isLocationPermissionGranted) {
            requestLocationUpdates()
        }
    }

    private fun requestLocationUpdates() {
        if (isGPSOn) {
            LocationHelperSecondary(requireContext(), object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    Common.myLocation = p0.lastLocation
                    Log.d("@@@@@@@@@", "onLocationResult: new location")
                }
            })
            animateMapToCurrentLocation()
        } else {
            turnOnGPS()
        }
    }

    private fun turnOnGPS() {

    }

    private fun animateMapToCurrentLocation() {
        if (Common.myLocation != null) {
            val latLng = LatLng(
                Common.myLocation?.latitude ?: Common.defLat,
                Common.myLocation?.longitude ?: Common.defLng
            )
            val cameraPosition = CameraPosition.builder().target(latLng).zoom(zoomValue)

            googleMap?.animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition.build()), 900, this
            )

            if (ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            googleMap?.isMyLocationEnabled = true

            binding.timeDepTv.text = resources.getString(R.string.waiting_time_5)
        }
    }

    override fun onCancel() {

    }

    override fun onFinish() {

    }

    private fun setOnBackPressed() {
//        activity?.onBackPressedDispatcher?.addCallback {
//            findNavController().popBackStack(R.id.homeFragment, false)
//        }
    }
}