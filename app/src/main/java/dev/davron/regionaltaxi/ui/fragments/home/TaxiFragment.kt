package dev.davron.regionaltaxi.ui.fragments.home

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
import com.google.android.gms.maps.model.Marker
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dev.davron.regionaltaxi.R
import dev.davron.regionaltaxi.databinding.FragmentTaxiBinding
import dev.davron.regionaltaxi.models.city.CitySelectedLocationModel
import dev.davron.regionaltaxi.ui.dialogs.fragmentBottomSheetDialogs.settingPermissionDialog.OpenSettingsBottomSheetFragment
import dev.davron.regionaltaxi.utils.Common
import dev.davron.regionaltaxi.utils.LocationHelperSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TaxiFragment : Fragment(), OnMapReadyCallback, GoogleMap.CancelableCallback,
    OpenSettingsBottomSheetFragment.ChangeListener {

    private lateinit var binding: FragmentTaxiBinding
    private lateinit var currentLocation: LatLng
    private var isLocationPermissionGranted: Boolean = false
    private var locationManager: LocationManager? = null
    private var isGPSOn: Boolean = false
    private val zoomValue: Float = 18f

    private var googleMap: GoogleMap? = null
    private var locationName: String? = null

    private var citySelectedLocationList = ArrayList<CitySelectedLocationModel>()
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
        onClickListener()
    }

    private fun onClickListener() {
        binding.regionBtn.setOnClickListener {
            findNavController().navigate(R.id.to_region_taxi)
        }

        binding.toWhereBtn.setOnClickListener {
            if (isLocationPermissionGranted) {
                if (citySelectedLocationList[0].name.isNotEmpty()) {
                    val bundle = Bundle()
                    bundle.putString("which", "to")
                    findNavController().navigate(R.id.to_map_taxi_where_to, bundle)
                } else {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.departure_place_unknown),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                openSettingsDialog()
            }
        }

        binding.nextBtn.setOnClickListener {
            if (isLocationPermissionGranted) {
                if (citySelectedLocationList[0].name.isNotEmpty()) {
                    findNavController().navigate(R.id.to_map_tariff)
                } else {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.departure_place_unknown),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                openSettingsDialog()
            }
        }
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

        this.googleMap?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoContents(p0: Marker): View? {
                locationName = p0.title.toString()
                return null
            }

            override fun getInfoWindow(p0: Marker): View? {
                return null
            }

        })
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
            if (isGPSOn) {
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
        }).withErrorListener {
            Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
        }.check()
    }

    private fun openSettingsDialog() {
        val openSettingsBottomSheetFragment = OpenSettingsBottomSheetFragment(
            R.drawable.ic_setting_location,
            resources.getString(R.string.permission_request_to_location),
            this
        )
        openSettingsBottomSheetFragment.show(childFragmentManager, "")
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

            removeShimmer()
            binding.timeDepTv.text = resources.getString(R.string.waiting_time_5)
            binding.addressNameTv.text = locationName
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

    override fun dismissListenerPermissionBottomSheet() {

    }

    private fun removeShimmer() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.INVISIBLE
        binding.currentAddressContainer.visibility = View.VISIBLE
    }
}