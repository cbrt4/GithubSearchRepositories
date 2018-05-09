package com.alex.githubsearchrepositories.util

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(context: Context) {

    private val fileName = "kithub.shared_preferences"

    private val sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    fun getPreference(preferenceKey: String) = sharedPreferences.getString(preferenceKey, "")

    fun setPreference(preferenceKey: String, preferenceValue: String) = sharedPreferences.edit().putString(preferenceKey, preferenceValue).apply()

}