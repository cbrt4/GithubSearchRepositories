package inc.alex.data.repo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "repos")
data class Repo(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,

    @SerializedName("full_name")
    @ColumnInfo(name = "name")
    val name: String?,

    @SerializedName("description")
    @ColumnInfo(name = "description")
    val description: String?,

    @SerializedName("updated_at")
    @ColumnInfo(name = "updated_at")
    val updatedAt: String?,

    @SerializedName("language")
    @ColumnInfo(name = "language")
    val language: String?,

    @SerializedName("watchers")
    @ColumnInfo(name = "watchers")
    val watchers: Int
)
