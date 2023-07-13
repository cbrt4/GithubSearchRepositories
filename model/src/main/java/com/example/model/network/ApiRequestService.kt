package com.example.model.network

import com.example.model.dto.search.SearchResponseEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequestService {

    @GET(SEARCH_REPOS)
    suspend fun searchRepos(
        @Query("q") searchQuery: String
    ): Response<SearchResponseEntity>
}
