package dev.davron.regionaltaxi.ui.fragments.home.mapTaxi.mapTariff

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.davron.regionaltaxi.R
import dev.davron.regionaltaxi.databinding.FragmentMapTariffBinding

class MapTariffFragment : Fragment() {

    private lateinit var binding: FragmentMapTariffBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapTariffBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        uiClickListener()
    }

    private fun init() {

    }

    private fun uiClickListener() {

    }
}