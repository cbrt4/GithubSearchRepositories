package com.alex.githubsearchrepositories.model.sharedpreferences

import android.content.Context

class SharedPreferences(context: Context) {

    private val fileName = "search_preferences"

    private val lastSearchQueryKey = "lastSearchQuery"

    private val sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    fun getLastSearchQuery(): String? =
            sharedPreferences.getString(lastSearchQueryKey, "")

    fun setLastSearchQuery(lastSearchQueryValue: String) =
            sharedPreferences.edit().putString(lastSearchQueryKey, lastSearchQueryValue).apply()
}