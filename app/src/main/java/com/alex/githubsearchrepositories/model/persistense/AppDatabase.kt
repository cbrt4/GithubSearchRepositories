package com.alex.githubsearchrepositories.model.persistense

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.alex.githubsearchrepositories.dto.repo.Repo
import com.alex.githubsearchrepositories.model.persistense.dao.RepoDao

@Database(entities = [(Repo::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao
}