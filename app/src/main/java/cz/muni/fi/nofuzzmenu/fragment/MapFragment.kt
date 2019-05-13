package cz.muni.fi.nofuzzmenu.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import android.graphics.drawable.Drawable
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import cz.muni.fi.nofuzzmenu.R


class MapFragment : Fragment() {
    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        Log.d("map", "on create")
    }
    // TODO: push ther fragment in in layout? so it's part of settings

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)

        childFragmentManager.beginTransaction()
            .replace(R.id.map, com.google.android.gms.maps.SupportMapFragment.newInstance())
            .commit()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.executePendingTransactions()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear() //clear old markers

            val googlePlex = CameraPosition.builder()
                // TODO use current location,
                // TODO implement lat/lon saving
                .target(LatLng(37.4220, 16.363000))
                .zoom(7f)
                .bearing(0f)
                //.tilt(45f)
                .build()
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1, null)
        }
    }

    private fun bitmapDescriptorFromVector(context: Context?, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context!!, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap =
            Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}