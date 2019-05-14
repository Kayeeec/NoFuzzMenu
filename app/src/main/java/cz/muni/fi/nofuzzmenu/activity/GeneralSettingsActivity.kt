package cz.muni.fi.nofuzzmenu.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.fragment.GeneralSettingsFragment

class GeneralSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_settings)

        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, GeneralSettingsFragment())
            .commit()
    }
}