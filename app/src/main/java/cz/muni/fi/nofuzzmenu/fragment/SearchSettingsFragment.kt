package cz.muni.fi.nofuzzmenu.fragment

import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat
import cz.muni.fi.nofuzzmenu.R

/**
 * The search_settings_root preference fragment that displays preferences that link to the other preference
 * fragments below.
 */
class SearchSettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Log.d("ss_fragment", "creating preferences with key $rootKey")
        setPreferencesFromResource(R.xml.search_settings_root, rootKey)
    }
}