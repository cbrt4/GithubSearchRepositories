package com.alex.githubsearchrepositories.util

import android.content.Context
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(context: Context) {

    private val fileName = "search_preferences"

    private val lastSearchQueryKey = "lastSearchQuery"

    private val sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    fun getLastSearchQuery(): Observable<String> {
        return Observable.fromCallable { sharedPreferences.getString(lastSearchQueryKey, "") }
    }

    fun setLastSearchQuery(lastSearchQueryValue: String) {
        sharedPreferences.edit().putString(lastSearchQueryKey, lastSearchQueryValue).apply()
    }
}