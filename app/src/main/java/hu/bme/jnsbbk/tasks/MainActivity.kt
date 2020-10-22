package hu.bme.jnsbbk.tasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarconfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        val controller = findNavController(R.id.nav_host_fragment)
        return controller.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val controller = findNavController(R.id.nav_host_fragment)
        if (controller.currentDestination!!.id == R.id.navItem_taskPagerFragment && item.itemId == android.R.id.home) {
            if (root_drawerLayout.isDrawerOpen(root_navView))
                root_drawerLayout.closeDrawers()
            else
                root_drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val controller = navHost.navController

        appBarconfig = AppBarConfiguration(controller.graph, root_drawerLayout)

        setupActionBarWithNavController(controller, appBarconfig)
        NavigationUI.setupWithNavController(root_navView, controller)
    }
}