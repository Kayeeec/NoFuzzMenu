package cz.muni.fi.nofuzzmenu.fragment

import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragment

class MapFragment : com.google.android.gms.maps.MapFragment() {

    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        Log.d("map", "on create")
    }
}