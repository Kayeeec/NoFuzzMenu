package cz.muni.fi.nofuzzmenu.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import cz.muni.fi.nofuzzmenu.R

/**
 * The search_settings_root preference fragment that displays preferences defined in an xml and
 * automatically saves them to shared preferences.
 */
class SearchSettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.search_settings_root, rootKey)
    }
}