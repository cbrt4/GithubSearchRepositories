package inc.alex.local.database

import inc.alex.data.repo.Repo
import javax.inject.Inject

class DataBaseRepository @Inject constructor(private val appDatabase: AppDatabase) {

    suspend fun insertRepos(repos: List<Repo>) = appDatabase.repoDao().insertRepos(repos)

    fun getRepos() = appDatabase.repoDao().getRepos()

    suspend fun clearRepos() = appDatabase.repoDao().clearRepos()
}
