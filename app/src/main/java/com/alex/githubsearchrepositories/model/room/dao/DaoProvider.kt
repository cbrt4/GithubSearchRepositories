package com.alex.githubsearchrepositories.model.room.dao

import android.arch.persistence.room.Room
import android.content.Context
import com.alex.githubsearchrepositories.model.room.AppDatabase

class DaoProvider {
    companion object {

        private var appDatabase: AppDatabase? = null

        private var repoDao: RepoDao? = null

        fun init(context: Context) {
            appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "repos-db")
                    .build()
        }

        fun repoDao(): RepoDao {
            if (repoDao == null) {
                repoDao = appDatabase?.repoDao()
            }

            return repoDao as RepoDao
        }
    }
}