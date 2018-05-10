package com.alex.githubsearchrepositories.network

import com.alex.githubsearchrepositories.model.search.SearchResponseEntity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequestService {

    @GET(SEARCH_REPOS)
    fun searchRepos(
            @Query("q") searchQuery: String
    ): Observable<SearchResponseEntity>
}