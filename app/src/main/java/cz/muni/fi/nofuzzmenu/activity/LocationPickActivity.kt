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
import android.location.LocationListener
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat

class LocationPickActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val LOCATION_PERMISSION = 10
        const val TAG = "Location pick activiry"
    }

    private lateinit var mMap: GoogleMap
    private val manager = getSystemService( Context.LOCATION_SERVICE ) as LocationManager
    var currentLocation: Location = Location(LocationManager.GPS_PROVIDER)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_pick)

        // check if GPS is on
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps()
        }
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           requestLocationPermission()
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // TODO: save lat, lon to shared Prefs on marker select?
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

        val startingLocation = LatLng(currentLocation.latitude, currentLocation.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startingLocation))
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes")
                { _, _ -> startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            currentLocation = location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            // NO-OP
        }

        override fun onProviderEnabled(provider: String?) {
            //NO-OP
        }

        override fun onProviderDisabled(provider: String?) {
            //NO-OP
        }
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
                        currentLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5f, locationListener)
                    } else {
                        setDefaultLocation()
                    }
                } else {
                    // current location unavailable, use default
                    Log.d("location pick", "Location permission not granted, using default of 0,0")
                    setDefaultLocation()
                }
            }
        }
    }

    private fun setDefaultLocation() {
        currentLocation.latitude = 0.0
        currentLocation.longitude = 0.0
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

}
