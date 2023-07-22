package com.alex.githubsearchrepositories.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import inc.alex.mvi.MviIntent
import inc.alex.mvi.MviState
import inc.alex.mvi.MviViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchViewState(
    val query: String = "",
    val isLoading: Boolean = false,
    val repos: List<inc.alex.data.repo.Repo> = emptyList()
) : MviState

sealed class SearchIntent : MviIntent {
    data class UpdateQuery(val query: String) : SearchIntent()
    object ToggleLoading : SearchIntent()
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val networkRepository: inc.alex.network.NetworkRepository,
    private val dataBaseRepository: inc.alex.local.database.DataBaseRepository,
    private val sharedPreferencesRepository: inc.alex.local.sharedpreferences.SharedPreferencesRepository
) : MviViewModel<SearchIntent, SearchViewState>(SearchViewState()) {

    private var currentJob: Job? = null
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        setState { copy(isLoading = false) }
        sendEvent(Event.LoadingError)
        Log.e(this::class.java.simpleName, "${exception.message}", exception)
    }

    override fun dispatchIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.UpdateQuery -> setState { copy(query = intent.query) }

            is SearchIntent.ToggleLoading -> {
                if (state.query.isEmpty()) {
                    sendEvent(Event.EmptyQuery)
                } else currentJob = if (state.isLoading) {
                    setState { copy(isLoading = false) }
                    currentJob?.cancel()
                    null
                } else {
                    setState { copy(isLoading = true) }
                    loadQuery()
                }
            }
        }
    }

    private fun loadQuery() = viewModelScope.launch(errorHandler) {
        if (sharedPreferencesRepository.getLastSearchQuery() == state.query) {
            loadLocalRepos()
        } else {
            loadRemoteRepos()
        }
    }

    private suspend fun loadRemoteRepos() {
        val query = state.query
        val response = networkRepository.loadRepos(query)
        if (response.isSuccessful) response.body()?.let { result ->
            sharedPreferencesRepository.saveLastSearchQuery(query)
            dataBaseRepository.clearRepos()
            dataBaseRepository.insertRepos(result.items)
            setState { copy(isLoading = false, repos = result.items) }
            if (result.items.isEmpty()) sendEvent(Event.NoResults)
        } else response.errorBody()?.let { error ->
            setState { copy(isLoading = false) }
            sendEvent(Event.LoadingError)
            Log.e(this::class.java.simpleName, error.string())
        }
    }

    private suspend fun loadLocalRepos() {
        dataBaseRepository.getRepos().collect {
            it?.let { repos ->
                setState { copy(isLoading = false, repos = repos) }
                if (repos.isEmpty()) sendEvent(Event.NoResults)
            } ?: run {
                setState { copy(isLoading = false) }
                sendEvent(Event.LoadingError)
            }
        }
    }

    sealed class Event {
        object EmptyQuery : Event()
        object NoResults : Event()
        object LoadingError : Event()
    }
}
