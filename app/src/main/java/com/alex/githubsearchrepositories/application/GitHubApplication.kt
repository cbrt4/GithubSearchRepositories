package com.alex.githubsearchrepositories.application

import android.app.Application
import com.alex.githubsearchrepositories.model.room.dao.DaoProvider
import com.alex.githubsearchrepositories.network.ApiRequestServiceProvider
import com.alex.githubsearchrepositories.sharedpreferences.SharedPreferencesProvider

class GitHubApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initComponents()
    }

    private fun initComponents() {
        DaoProvider.init(this)
        ApiRequestServiceProvider.init(this)
        SharedPreferencesProvider.init(this)
    }
}