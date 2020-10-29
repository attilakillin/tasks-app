package hu.bme.jnsbbk.tasks

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import hu.bme.jnsbbk.tasks.data.AppDatabase
import hu.bme.jnsbbk.tasks.data.ThemePreferences
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemePreferences.initialize(this)
        loadTheme()
        setContentView(R.layout.activity_main)
        AppDatabase.initialize(this)
        setupNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        val controller = findNavController(R.id.nav_host_fragment)
        return controller.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val controller = findNavController(R.id.nav_host_fragment)
        if (controller.currentDestination!!.id == R.id.navItem_taskPagerFragment && item.itemId == android.R.id.home) {
            if (root_drawerLayout.isDrawerOpen(root_navView))
                root_drawerLayout.closeDrawers()
            else
                root_drawerLayout.openDrawer(GravityCompat.START)
            return true
        } else if (item.itemId == R.id.menuItem_darkMode) {
            ThemePreferences.toggleDarkMode()
            loadTheme()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val controller = navHost.navController

        val appBarConfig = AppBarConfiguration(controller.graph, root_drawerLayout)

        setupActionBarWithNavController(controller, appBarConfig)
        NavigationUI.setupWithNavController(root_navView, controller)
    }

    private fun loadTheme() {
        if (ThemePreferences.darkMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}