package inc.alex.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import inc.alex.data.repo.Repo

@Database(entities = [(Repo::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao
}
