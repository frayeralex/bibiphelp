package com.github.frayeralex.bibiphelp.cache

import android.content.Context
import android.content.SharedPreferences
import com.github.frayeralex.bibiphelp.App

abstract class BaseSharedPreferencesManager(context: App, preferences: String) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(preferences, Context.MODE_PRIVATE)
    }

    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String) = sharedPreferences.getString(key, DEFAULT_STRING)!!

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String) = sharedPreferences.getBoolean(key, DEFAULT_BOOL)

    fun saveFloat(key: String, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    fun getFloat(key: String, def: Float = 0f) = sharedPreferences.getFloat(key, def)

    companion object {
        const val DEFAULT_STRING = ""
        const val DEFAULT_BOOL = false
    }
}