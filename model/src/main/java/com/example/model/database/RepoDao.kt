package com.example.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.model.dto.repo.Repo
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepos(repos: List<Repo>)

    @Query("SELECT * FROM repos")
    fun getRepos(): Flow<List<Repo>?>

    @Query("DELETE FROM repos")
    suspend fun clearRepos()
}
