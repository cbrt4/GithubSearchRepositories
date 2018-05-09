package com.alex.githubsearchrepositories.dagger.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.alex.githubsearchrepositories.model.room.AppDatabase
import com.alex.githubsearchrepositories.model.room.dao.RepoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(val context: Context) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = context

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "repos-db").build()

    @Provides
    fun provideRepoDao(database: AppDatabase): RepoDao = database.repoDao()
}