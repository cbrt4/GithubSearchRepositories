package com.alex.githubsearchrepositories.application

import android.app.Application
import com.alex.githubsearchrepositories.dagger.components.ApplicationComponent
import com.alex.githubsearchrepositories.dagger.components.DaggerApplicationComponent
import com.alex.githubsearchrepositories.dagger.modules.ApplicationModule

class KitHubApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        initApplicationComponent()
    }

    private fun initApplicationComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    fun getSharedPreferencesManager() = applicationComponent.sharedPreferences()
}