package apps.monkpad.kiddoguard.data

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val PREFERENCES_NAME = "KiddoGuardPreferences"
    private const val KEY_SELECTED_APPS = "selected_apps"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun saveSelectedApps(context: Context, selectedApps: Set<String>) {
        val prefs = getPreferences(context)
        prefs.edit().putStringSet(KEY_SELECTED_APPS, selectedApps).apply()
    }

    fun getSelectedApps(context: Context): Set<String> {
        val prefs = getPreferences(context)
        return prefs.getStringSet(KEY_SELECTED_APPS, emptySet()) ?: emptySet()
    }
}