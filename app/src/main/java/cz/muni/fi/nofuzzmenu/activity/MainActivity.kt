package cz.muni.fi.nofuzzmenu.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.fragment.RestaurantListFragment

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        /*
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/

        // initialize restaurant view fragment
        if (savedInstanceState == null) {
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().replace(android.R.id.content,
                RestaurantListFragment(),
                RestaurantListFragment::class.java.simpleName).commit()
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("main", "resuming main")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_search_settings -> {
                startActivity(Intent(this, SearchSettingsActivity::class.java))
                Log.d("main", "started settings activity")
                return true
            }
            R.id.action_settings -> {
                startActivity(Intent(this, GeneralSettingsActivity::class.java))
                Log.d("main", "started general settings activity")
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
