package com.abhinav.notesapplication.util

import android.content.Context
import android.content.SharedPreferences

private const val MY_PREFERENCES = "myPreferences"
const val PREF_LOGIN = "loggedInUser"
const val PREF_IS_LOGGED_IN = "isLoggedIn"
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

    fun getId(key: String, defaultValue: String = ""): String {
        return sharedPref.getString(key, defaultValue)!!
    }

    fun saveId(key: String, value: String) {
        editor.putString(key,value)
        editor.apply()
    }
}