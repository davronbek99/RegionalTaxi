package dev.davron.regionaltaxi.ui.fragments.home.mapTaxi.mapTaxiWhereTo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.davron.regionaltaxi.R
import dev.davron.regionaltaxi.databinding.FragmentMapTaxiWhereToBinding

class MapTaxiWhereToFragment : Fragment() {

    private lateinit var binding: FragmentMapTaxiWhereToBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapTaxiWhereToBinding.inflate(inflater, container, false)

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