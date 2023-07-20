package com.example.model.sharedpreferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferencesRepository @Inject constructor(@ApplicationContext context: Context) {

    private val fileName = "search_preferences"

    private val lastSearchQueryKey = "lastSearchQuery"

    private val sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    fun getLastSearchQuery(): String? = sharedPreferences.getString(lastSearchQueryKey, null)

    fun saveLastSearchQuery(lastSearchQueryValue: String) =
        sharedPreferences.edit().putString(lastSearchQueryKey, lastSearchQueryValue).apply()
}