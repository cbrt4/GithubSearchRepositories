package inc.alex.data.search

import com.google.gson.annotations.SerializedName
import inc.alex.data.repo.Repo

data class SearchResponseEntity(

    @SerializedName("total_count")
    val totalCount: Long,

    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @SerializedName("items")
    val items: List<Repo>
)
