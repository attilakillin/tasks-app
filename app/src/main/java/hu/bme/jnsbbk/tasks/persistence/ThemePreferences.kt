package hu.bme.jnsbbk.tasks.persistence

import android.content.Context
import android.content.SharedPreferences
import hu.bme.jnsbbk.tasks.R

object ThemePreferences {
    var darkMode: Boolean = false
        private set(value) {
            field = value
            prefs.edit().putBoolean("dark_mode", value).apply()
        }

    private lateinit var prefs: SharedPreferences

    fun initialize(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(context.getString(R.string.preferences), Context.MODE_PRIVATE)
        darkMode = prefs.getBoolean("dark_mode", false)
    }

    fun toggleDarkMode() {
        darkMode = !darkMode
    }
}