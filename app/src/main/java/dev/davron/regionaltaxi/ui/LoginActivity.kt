package dev.davron.regionaltaxi.ui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dev.davron.regionaltaxi.R
import dev.davron.regionaltaxi.broadcast.NetworkingBroadcastReceiver
import dev.davron.regionaltaxi.broadcast.UpdateUI
import dev.davron.regionaltaxi.databinding.ActivityLoginBinding
import dev.davron.regionaltaxi.extensions.getAutoNightMode
import dev.davron.regionaltaxi.extensions.statusBarColor
import dev.davron.regionaltaxi.utils.MySharedPreferences
import dev.davron.regionaltaxi.utils.broadcastReciever.SmsBroadcastReciever
import java.text.SimpleDateFormat
import java.util.Date

class LoginActivity : AppCompatActivity() ,UpdateUI{
    private lateinit var binding: ActivityLoginBinding

    private lateinit var sharedPreferences: MySharedPreferences
    private lateinit var navController: NavController

    private lateinit var broadcastReceiver: NetworkingBroadcastReceiver
    private lateinit var smsReciever: SmsBroadcastReciever
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        init()
        setUpUI()
        adjustFontScale(resources.configuration)
    }

    private fun init() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        broadcastReceiver = NetworkingBroadcastReceiver(this)
        smsReciever = SmsBroadcastReciever()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setUpUI() {
        var nightMode = MySharedPreferences.getNightMode(this)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.phoneFragment -> {
                    //change status bar color
                    statusBarColor(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.phone_status_bar_color,
                            theme
                        ),
                        ResourcesCompat.getColor(
                            resources,
                            R.color.phone_status_bar_color,
                            theme
                        ), nightMode != "night"
                    )
                }

                R.id.smsCodeFragment -> {
                    //change status bar color
                    statusBarColor(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.phone_status_bar_color,
                            theme
                        ),
                        ResourcesCompat.getColor(
                            resources,
                            R.color.phone_status_bar_color,
                            theme
                        ), nightMode != "night"
                    )
                }

                else -> {
                    //change status bar color
                    changeStatusBarColor()
                }
            }
        }
    }

    private fun changeStatusBarColor() {

        var nightMode = MySharedPreferences.getNightMode(this)

        if (nightMode == "auto") nightMode = getAutoNightMode()

        statusBarColor(
            ResourcesCompat.getColor(resources, R.color.phone_status_bar_color, theme),
            ResourcesCompat.getColor(resources, R.color.phone_status_bar_color, theme),
            nightMode != "night"
        )
    }

    private fun setTheme() {
        when (MySharedPreferences.getNightMode(this)) {
            "day" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            "night" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            "auto" -> {
                setListeners()
                val newNightMode = getAutoNightMode()

                when (newNightMode) {
                    "day" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }

                    "night" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
            }
        }
        changeStatusBarColor()
    }

    private fun setListeners() {
        val intentFilter = IntentFilter(Intent.ACTION_TIME_TICK)
        registerReceiver(timerReciever, intentFilter)
    }

    private val timerReciever = object : BroadcastReceiver() {
        @SuppressLint("SimpleDateFormat")
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1 != null) {
                if (p1.action == Intent.ACTION_TIME_TICK) {
                    val time = Date()
                    val dH = SimpleDateFormat("HH")
                    val dM = SimpleDateFormat("mm")
                    val hour = dH.format(time).toInt()
                    val minute = dM.format(time).toInt()
                    val nightMode = MySharedPreferences.getNightMode(this@LoginActivity)

                    if (nightMode == "auto") {
                        if (hour == 18 || hour == 6) {
                            if (minute == 0) {
//                                finishAffinity()
//                                val intent =
//                                    Intent(this@MainActivity, MainActivity::class.java)
//                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun adjustFontScale(configuration: Configuration) {
        if (configuration.fontScale > 1f || configuration.fontScale < 1f) {
            configuration.fontScale = 1f
            val metrics = resources.displayMetrics
            val wm = getSystemService(WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        registerReceiver(smsReciever, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
        unregisterReceiver(smsReciever)
    }

    override fun isOnLine(value: Boolean) {
        if (!value) {
            startActivity(Intent(this, NetworkReceiverActivity::class.java))
        }
    }
}