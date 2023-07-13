package com.example.model.dto.search

import com.example.model.dto.repo.Repo
import com.google.gson.annotations.SerializedName

data class SearchResponseEntity(

    @SerializedName("total_count")
    val totalCount: Long,

    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @SerializedName("items")
    val items: List<Repo>
)
