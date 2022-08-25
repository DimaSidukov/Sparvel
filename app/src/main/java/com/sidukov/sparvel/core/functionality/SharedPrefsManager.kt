package com.sidukov.sparvel.core.functionality

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsManager(context: Context) {

    companion object {
        private const val APP_PREFERENCES = "app_preferences"
        private const val APP_THEME = "app_theme"
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    var appTheme: Int
        get() = preferences.getInt(APP_THEME, AppTheme.DARK.code)
        set(value) = preferences.edit().putInt(APP_THEME, value).apply()
}

enum class AppTheme(val code: Int) {
    LIGHT(0), DARK(1)
}