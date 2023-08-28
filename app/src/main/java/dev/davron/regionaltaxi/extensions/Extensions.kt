package dev.davron.regionaltaxi.extensions

import android.annotation.SuppressLint
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.fragment.app.FragmentActivity
import java.text.SimpleDateFormat
import java.util.Date


fun FragmentActivity.statusBarColor(
    @ColorInt statusBarColor: Int,
    @ColorInt navigationBarColor: Int,
    darkStatusBarTint: Boolean
) {
    val win: Window = (window).also {
        it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        it.statusBarColor = statusBarColor
    }

    val dec = win.decorView
    if (darkStatusBarTint) {
        dec.systemUiVisibility = dec.systemUiVisibility or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        dec.systemUiVisibility = 0
    }
}

@SuppressLint("SimpleDateFormat")
fun FragmentActivity.getAutoNightMode(): String {
    val hour = Date()
    val df = SimpleDateFormat("HH")
    val string = df.format(hour).toInt()

    return if (string < 6 || string > 18) "night" else "day"
}