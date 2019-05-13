package cz.muni.fi.nofuzzmenu.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.fragment.SearchSettingsFragment

class RestaurantDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_restaurant_detail)
        Log.d("rd_activity", "before fragment")
        /*
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, SearchSettingsFragment())
            .commit()
*/
        Log.d("rd_activity", "after fragment")
    }
}