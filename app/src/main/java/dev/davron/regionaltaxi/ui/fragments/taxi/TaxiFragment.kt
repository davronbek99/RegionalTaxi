package dev.davron.regionaltaxi.ui.fragments.taxi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import dev.davron.regionaltaxi.R
import dev.davron.regionaltaxi.databinding.FragmentTaxiBinding
import dev.davron.regionaltaxi.utils.Common

class TaxiFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentTaxiBinding
    private lateinit var currentLocation: LatLng
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

    }

    override fun onMapReady(googleMap: GoogleMap) {

    }
}