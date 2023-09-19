package dev.davron.regionaltaxi.ui.fragments.regionTaxi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.prolificinteractive.materialcalendarview.CalendarDay
import dev.davron.regionaltaxi.R
import dev.davron.regionaltaxi.adapters.regionTaxi.RegionRecentSearchRvAdapter
import dev.davron.regionaltaxi.databinding.FragmentRegionTaxiBinding
import dev.davron.regionaltaxi.extensions.returnSelectedDate
import dev.davron.regionaltaxi.models.regionTaxi.RecentSearchTaxiModel
import dev.davron.regionaltaxi.utils.Common
import java.util.Calendar
import java.util.TimeZone

class RegionTaxiFragment : Fragment() ,RegionRecentSearchRvAdapter.OnItemClickListener{

    private lateinit var binding: FragmentRegionTaxiBinding
    private lateinit var regionRecentSearchRvAdapter: RegionRecentSearchRvAdapter
    private val TAG = "RegionTaxiFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegionTaxiBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setUpUI()
        setBackPressed()
        uiClickListener()
        setRecentSearchRvAdapter()
    }

    private fun uiClickListener() {

    }

    private fun init() {

    }

    @SuppressLint("SetTextI18n")
    private fun setUpUI() {
        if (Common.selectedDate == null) {
            binding.dateTv.text = getCurrentDate() + " (${resources.getString(R.string.today)})"
            Common.selectedDate = CalendarDay.today()
        } else {
            val date = Common.selectedDate
            binding.dateTv.text = returnSelectedDate(
                requireContext(),
                date?.day ?: 0,
                date?.month ?: 0,
                date?.year ?: 0
            )
        }

    }

    private fun setRecentSearchRvAdapter() {

    }

    private fun setBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback {
            findNavController().popBackStack()
        }
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        Log.d(TAG, "getCurrentDate:  $day $month $year, ${TimeZone.getDefault()}")
        Log.d(TAG, "getCurrentDate:  ${day + 1} $month $year")

        return returnSelectedDate(requireContext(), day, month + 1, year)
    }

    override fun onItemClick(model: RecentSearchTaxiModel, position: Int) {
        Common.fromCity = model.fromDistrict
        Common.fromDistrictId = model.fromDistrictId
        Common.toCity = model.toDistrict
        Common.toDistrictId = model.toDistrictId
//        findNavController().navigate(R.id.to_found_region_taxi_fragment)
    }
}