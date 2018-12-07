package com.alex.githubsearchrepositories.dto.search

import com.alex.githubsearchrepositories.dto.repo.Repo
import com.google.gson.annotations.SerializedName

data class SearchResponseEntity(

        @SerializedName("total_count")
        val totalCount: Long,

        @SerializedName("incomplete_results")
        val incompleteResults: Boolean,

        @SerializedName("items")
        val items: ArrayList<Repo>
)