package com.alex.githubsearchrepositories.model.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.alex.githubsearchrepositories.model.repo.Repo

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepos(repos: List<Repo>)

    @Query("SELECT * FROM repos")
    fun getRepos(): LiveData<List<Repo>>

    @Query("DELETE FROM repos")
    fun clearRepos()
}