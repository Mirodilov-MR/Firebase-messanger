package com.example.firebase_messanger.activities

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    private const val PREF_NAME = "MyPrefs"
    private const val KEY_IS_LOGGED_IN = "isLoggedIn"

    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        val editor: SharedPreferences.Editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
}
