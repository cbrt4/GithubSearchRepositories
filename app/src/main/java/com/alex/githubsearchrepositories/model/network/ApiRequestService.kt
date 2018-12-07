package com.alex.githubsearchrepositories.model.network

import com.alex.githubsearchrepositories.dto.search.SearchResponseEntity
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequestService {

    @GET(SEARCH_REPOS)
    fun searchRepos(
            @Query("q") searchQuery: String
    ): Deferred<Response<SearchResponseEntity>>
}