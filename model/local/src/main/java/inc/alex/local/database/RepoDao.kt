package inc.alex.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import inc.alex.data.repo.Repo
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDao {

    @Insert(entity = Repo::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepos(repos: List<Repo>)

    @Query("SELECT * FROM repos")
    fun getRepos(): Flow<List<Repo>?>

    @Query("DELETE FROM repos")
    suspend fun clearRepos()
}
