package cz.muni.fi.nofuzzmenu.fragment

import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat
import cz.muni.fi.nofuzzmenu.R

class RestaurantDetailFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Log.d("rd_fragment", "creating preferences with key $rootKey")
    }
}