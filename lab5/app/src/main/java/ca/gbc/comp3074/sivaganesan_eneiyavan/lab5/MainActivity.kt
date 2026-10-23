package ca.gbc.comp3074.sivaganesan_eneiyavan.lab5

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var locationManager: LocationManager
    private lateinit var statusText: TextView
    private lateinit var latitudeText: TextView
    private lateinit var longitudeText: TextView

    // Ask Android to show the location permission prompt when needed.
    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allowed = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (allowed) {
            startLocationUpdates()
        } else {
            statusText.text = getString(R.string.location_permission_denied)
        }
    }

    // Receive each new GPS position and show its coordinates.
    private val locationListener = LocationListener { location ->
        showLocation(location)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        latitudeText = findViewById(R.id.latitudeText)
        longitudeText = findViewById(R.id.longitudeText)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.refreshButton).setOnClickListener {
            checkPermissionAndStart()
        }

        checkPermissionAndStart()
    }

    // Check permission before asking Android for the device location.
    private fun checkPermissionAndStart() {
        val hasFineLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasFineLocation || hasCoarseLocation) {
            startLocationUpdates()
        } else {
            permissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Listen for GPS readings while this screen is open.
    @Suppress("MissingPermission")
    private fun startLocationUpdates() {
        if (!hasLocationPermission()) {
            checkPermissionAndStart()
            return
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            statusText.text = getString(R.string.location_gps_disabled)
            return
        }

        statusText.text = getString(R.string.location_searching)
        try {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let(::showLocation)
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                LOCATION_UPDATE_INTERVAL_MS,
                0f,
                locationListener,
                Looper.getMainLooper()
            )
        } catch (_: SecurityException) {
            statusText.text = getString(R.string.location_permission_denied)
        } catch (_: IllegalArgumentException) {
            statusText.text = getString(R.string.location_gps_unavailable)
        }
    }

    // Put the latest latitude and longitude on the screen.
    private fun showLocation(location: Location) {
        latitudeText.text = String.format(Locale.US, "%.6f", location.latitude)
        longitudeText.text = String.format(Locale.US, "%.6f", location.longitude)
        statusText.text = getString(R.string.location_updated)
    }

    // Check whether the user allowed either precise or approximate location.
    private fun hasLocationPermission(): Boolean {
        val fineAllowed = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseAllowed = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fineAllowed || coarseAllowed
    }

    override fun onResume() {
        super.onResume()
        if (::locationManager.isInitialized && hasLocationPermission()) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        // Stop GPS updates when the app is not on screen to save battery.
        if (::locationManager.isInitialized && hasLocationPermission()) {
            locationManager.removeUpdates(locationListener)
        }
    }

    companion object {
        private const val LOCATION_UPDATE_INTERVAL_MS = 2_000L
    }
}
