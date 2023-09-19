package dev.davron.regionaltaxi.utils

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.prolificinteractive.materialcalendarview.CalendarDay

object Common {

    var defLat = 41.306551
    var defLng = 69.240596

    var myLocation: Location? = null
//    var myDetails: SelectedContact? = null

    var selectedDate: CalendarDay? = null
    var fromDistrictId: Int = -1
    var toDistrictId: Int = -1
    val totalFoundTaxi = MutableLiveData<String>()

    var fromCity: String = ""
    var toRegion: String = ""
    var toCity: String = ""
}