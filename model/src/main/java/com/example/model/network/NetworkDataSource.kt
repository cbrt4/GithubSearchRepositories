package com.example.model.network

import javax.inject.Inject

class NetworkDataSource @Inject constructor(private val apiRequestService: ApiRequestService) {

    suspend fun loadRepos(searchQuery: String) = apiRequestService.searchRepos(searchQuery)
}