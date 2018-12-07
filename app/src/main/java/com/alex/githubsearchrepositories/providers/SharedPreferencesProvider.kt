package com.alex.githubsearchrepositories.providers

import android.content.Context
import com.alex.githubsearchrepositories.model.sharedpreferences.SharedPreferences

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