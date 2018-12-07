package com.alex.githubsearchrepositories.model.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.alex.githubsearchrepositories.model.repo.Repo
import com.alex.githubsearchrepositories.model.room.dao.RepoDao

@Database(entities = [(Repo::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao
}