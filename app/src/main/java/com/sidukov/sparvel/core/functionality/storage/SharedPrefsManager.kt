package com.sidukov.sparvel.core.functionality.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class SharedPrefsManager @Inject constructor(context: Context) {

    companion object {
        private const val APP_PREFERENCES = "app_preferences"
        private const val APP_THEME = "app_theme"
        private const val TRACK_SELECTED = "track_selected"
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    var appTheme: Int
        get() = preferences.getInt(APP_THEME, AppTheme.DARK.code)
        set(value) = preferences.edit { putInt(APP_THEME, value) }

    var trackId: String?
        get() = preferences.getString(TRACK_SELECTED, null)
        set(value) = preferences.edit { putString(TRACK_SELECTED, value) }
}

enum class AppTheme(val code: Int) {
    LIGHT(0), DARK(1)
}