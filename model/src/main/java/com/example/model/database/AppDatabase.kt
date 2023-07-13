package com.example.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.model.dto.repo.Repo

@Database(entities = [(Repo::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao
}
