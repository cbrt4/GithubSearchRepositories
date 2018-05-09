package com.alex.githubsearchrepositories.dagger.components

import android.content.Context
import com.alex.githubsearchrepositories.dagger.modules.ApplicationModule
import com.alex.githubsearchrepositories.dagger.modules.NetworkModule
import com.alex.githubsearchrepositories.model.room.dao.RepoDao
import com.alex.githubsearchrepositories.network.ApiRequestService
import com.alex.githubsearchrepositories.util.SharedPreferencesManager
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class])
interface ApplicationComponent {

    fun context(): Context

    fun sharedPreferences(): SharedPreferencesManager

    fun apiRequestService(): ApiRequestService

    fun repoDao(): RepoDao
}