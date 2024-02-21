package com.abhinav.notesapplication.util

import android.content.Context
import android.content.SharedPreferences

private const val MY_PREFERENCES = "myPreferences"
const val PREF_LOGIN = "loggedInUser"
const val PREF_LOGGED_IN = "isLoggedIn"
class PreferenceManager(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPref.getBoolean(key, defaultValue)
    }
    fun saveBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPref.getInt(key, defaultValue)
    }

    fun saveInt(key: String, value: Int) {
        editor.putInt(key,value)
        editor.apply()
    }
}