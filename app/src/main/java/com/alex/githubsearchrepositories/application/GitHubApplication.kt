package com.alex.githubsearchrepositories.application

import android.app.Application
import com.alex.githubsearchrepositories.providers.ApiRequestServiceProvider
import com.alex.githubsearchrepositories.providers.DaoProvider
import com.alex.githubsearchrepositories.providers.SharedPreferencesProvider

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