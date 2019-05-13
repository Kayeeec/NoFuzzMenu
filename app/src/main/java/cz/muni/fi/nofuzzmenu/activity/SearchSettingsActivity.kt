package cz.muni.fi.nofuzzmenu.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.fragment.MapFragment
import cz.muni.fi.nofuzzmenu.fragment.SearchSettingsFragment

class SearchSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_settings)

        supportFragmentManager.beginTransaction()
            .replace(R.id.location_settings, MapFragment())
            .commit()
        supportFragmentManager.beginTransaction()
            .replace(R.id.search_settings, SearchSettingsFragment())
            .commit()
    }
}