package cz.muni.fi.nofuzzmenu.activity

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import cz.muni.fi.nofuzzmenu.R
import android.location.LocationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AlertDialog
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class LocationPickActivity : AppCompatActivity(), OnMapReadyCallback  {

    companion object {
        const val LOCATION_PERMISSION = 10
        const val TAG = "Location pick activity"
    }

    private lateinit var mMap: GoogleMap
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var currentLocation: Location = Location(LocationManager.GPS_PROVIDER)
    var defaultLocation: Location = Location(LocationManager.GPS_PROVIDER)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_pick)
        setDefaultLocation()

        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(60 * 1000)
            .setFastestInterval(30 * 1000)

        // from: https://developer.android.com/training/location/receive-location-updates.html
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    Log.d(TAG, "Handling location update callback: setting location to $location")
                    currentLocation = location
                    val startingLocation = LatLng(currentLocation.latitude, currentLocation.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingLocation, 5f))
                    // TODO: after first receive, unsubscribe, subscribe in onResume
                    // TODO: add recenter button
                }
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // TODO (priority): save lat, lon to shared Prefs ... when?
    }

    override fun onStart() {
        super.onStart()
        setStartingLocation()
        Log.d(TAG, "OnStart currentLocation: $currentLocation")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // clear markers
        mMap.clear()
        // Don't set any camera view here, as this method is called very early
    }

    private fun requestLocationPermission() {
        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            showLocationPermissionRationale(this)
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // can work with location now
                    Log.i(TAG, "Location permission granted")
                    if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null)
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location : Location? ->
                                Log.d(TAG, "Setting current location to $location")
                                currentLocation = location ?: defaultLocation
                            }
                    }
                } else {
                    // current location unavailable, use default
                    Log.d("location pick", "Location permission not granted, using default of 0,0")
                    currentLocation = defaultLocation
                }
            }
        }
    }

    private fun setDefaultLocation() {
        defaultLocation.latitude = 0.0
        defaultLocation.longitude = 0.0
    }

    private fun showLocationPermissionRationale(context: Context) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Location permission request")
        alertBuilder.setMessage("The app automatically searches for restaurants around you. Without your location" +
                "we can not suggest relevant restaurants.")
        alertBuilder.setPositiveButton(android.R.string.ok) { dialog, which ->
            // go to settings page to set the permission
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        val alert = alertBuilder.create()
        alert.show()
    }

    private fun setStartingLocation() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
        } else {
            Log.d(TAG, "Fine location granted, getting last location")
            fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    currentLocation = location ?: defaultLocation
                    Log.d(TAG, "Set current location to $currentLocation (location was $location)")
                }
        }
    }

}
