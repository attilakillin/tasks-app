package hu.bme.jnsbbk.tasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import hu.bme.jnsbbk.tasks.debug.CategoryGenerator
import hu.bme.jnsbbk.tasks.persistence.db.AppDatabase
import hu.bme.jnsbbk.tasks.persistence.ThemePreferences
import hu.bme.jnsbbk.tasks.debug.TaskGenerator
import hu.bme.jnsbbk.tasks.fragments.tasklist.TaskPagerFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemePreferences.initialize(this)
        loadTheme()
        setContentView(R.layout.activity_main)
        AppDatabase.initialize(this)
        setupNavigation()
        thread { AppDatabase.INSTANCE.categoryDao().checkAndInsertNoCategory() }
    }

    override fun onSupportNavigateUp(): Boolean {
        val controller = findNavController(R.id.nav_host_fragment)
        return controller.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val controller = findNavController(R.id.nav_host_fragment)
        when (item.itemId) {
            android.R.id.home -> {
                if (controller.currentDestination!!.id == R.id.navItem_taskPagerFragment) {
                    if (root_drawerLayout.isDrawerOpen(root_navView))
                        root_drawerLayout.closeDrawers()
                    else
                        root_drawerLayout.openDrawer(GravityCompat.START)
                    return true
                }
            }
            R.id.menuItem_darkMode -> {
                ThemePreferences.toggleDarkMode()
                loadTheme()
            }
            R.id.menuItem_generate -> {
                thread {
                    val success = TaskGenerator.generateTask()
                    if (!success) runOnUiThread {
                        Toast.makeText(
                            this, "Can't generate random task: Please create a category first!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            R.id.menuItem_catGenerate -> {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.default_category_generate_title))
                    .setMessage(getString(R.string.default_category_generate_message))
                    .setPositiveButton("OK") { _, _ -> CategoryGenerator.generateDefaultCategories(this) }
                    .setNegativeButton("Cancel") { _, _ -> }
                    .create()
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (root_drawerLayout.isDrawerOpen(root_navView)) {
            root_drawerLayout.closeDrawers()
            return
        }

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val current = navHost.childFragmentManager.primaryNavigationFragment
        if (current is TaskPagerFragment && current.processBackButtonPress()) {
            return // Ekkor sikerült a TaskPagerFragmentnek feldolgoznia a vissza gomb kérést
        }

        super.onBackPressed()
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