package com.alex.githubsearchrepositories.model.room.dao

import android.arch.persistence.room.*
import com.alex.githubsearchrepositories.model.repo.RepoEntity
import io.reactivex.Flowable

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepos(repos: List<RepoEntity>)

    @Query("SELECT * FROM repos")
    fun getAllTasks(): Flowable<List<RepoEntity>>
}