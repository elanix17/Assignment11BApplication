package com.example.assignment11bapplication

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.work.WorkManager
import com.example.assignment11bapplication.workmanager.ApiWorker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity() {
    private var currentWeatherIcon: Drawable? = null
    private var cityName: String? = null
    private var temperature: String? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView1) as NavHostFragment

        val navController = navHost.navController
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.my_nav)
        navController.graph = graph

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocation()
        viewModel.weatherIcon.observe(this) {
            currentWeatherIcon = it
            invalidateOptionsMenu()
        }

        viewModel.cityName.observe(this) {
            cityName = it
            invalidateOptionsMenu()
        }

        viewModel.temperature.observe(this) {
            temperature = it
            invalidateOptionsMenu()
            enqueueWorkRequest()
        }
    }

    private fun requestLocationPermission() {
        val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) requestLocation()
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                val locationTask: Task<Location> = fusedLocationClient.lastLocation
                locationTask.addOnSuccessListener { location ->
                    location?.let {
                        viewModel.fetchWeatherData(it.latitude, it.longitude)
                    }
                }
                locationTask.addOnFailureListener { exception ->
                    // Log the exception or handle it as necessary
                    Log.e("MainActivity", "Error retrieving location: ${exception.message}")
                }
            } catch (e: SecurityException) {
                Log.e("MainActivity", "SecurityException: ${e.message}")
            }
        } else {
            // Request permission if it hasn’t been granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val weatherItem = menu.findItem(R.id.menu_weather)
        val actionView = weatherItem?.actionView ?: layoutInflater.inflate(R.layout.weather_menu_item, null)

        // Bind the icon and temperature dynamically
        val iconImageView = actionView.findViewById<ImageView>(R.id.iconImageView)
        val tempTextView = actionView.findViewById<TextView>(R.id.tempTextView)
        val cityTextView = actionView.findViewById<TextView>(R.id.cityTextView)

        // Set your weather icon and temperature (ensure currentWeatherIcon, temperature are not null)
        iconImageView.setImageDrawable(currentWeatherIcon)  // Set your fetched icon
        tempTextView.text = "$temperature"  // Set your fetched temperature
        cityTextView.text = cityName

        // Update the action view for the menu item
        weatherItem?.actionView = actionView
        return super.onPrepareOptionsMenu(menu)
    }
    private fun enqueueWorkRequest() {
        val workRequest = ApiWorker.createWorkRequest()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}

