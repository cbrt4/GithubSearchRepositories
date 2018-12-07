package com.alex.githubsearchrepositories.sharedpreferences

import android.content.Context

class SharedPreferencesProvider {
    companion object {

        private var sharedPreferencesManager: SharedPreferences? = null

        fun init(context: Context) {
            sharedPreferencesManager = SharedPreferences(context)
        }

        fun sharedPreferences(): SharedPreferences {
            return sharedPreferencesManager as SharedPreferences
        }
    }
}