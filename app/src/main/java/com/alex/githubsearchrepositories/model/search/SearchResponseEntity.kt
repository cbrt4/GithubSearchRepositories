package com.alex.githubsearchrepositories.model.search

import com.alex.githubsearchrepositories.model.repo.RepoEntity
import com.google.gson.annotations.SerializedName

data class SearchResponseEntity(

        @SerializedName("total_count")
        val totalCount: Long,

        @SerializedName("incomplete_results")
        val incompleteResults: Boolean,

        @SerializedName("items")
        val items: ArrayList<RepoEntity>
)