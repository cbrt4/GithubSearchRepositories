package com.example.model.database

import com.example.model.dto.repo.Repo
import javax.inject.Inject

class DataBaseDataSource @Inject constructor(private val appDatabase: AppDatabase) {

    suspend fun insertRepos(repos: List<Repo>) = appDatabase.repoDao().insertRepos(repos)

    suspend fun getRepos() = appDatabase.repoDao().getRepos()

    suspend fun clearRepos() = appDatabase.repoDao().clearRepos()
}
