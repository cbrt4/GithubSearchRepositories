package inc.alex.network

import javax.inject.Inject

class NetworkRepository @Inject constructor(private val apiRequestService: ApiRequestService) {

    suspend fun loadRepos(searchQuery: String) = apiRequestService.searchRepos(searchQuery)
}