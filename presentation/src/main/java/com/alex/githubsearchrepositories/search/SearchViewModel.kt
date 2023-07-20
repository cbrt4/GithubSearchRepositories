package com.alex.githubsearchrepositories.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.model.database.DataBaseDataSource
import com.example.model.dto.repo.Repo
import com.example.model.network.NetworkRepository
import com.example.model.sharedpreferences.SharedPreferencesRepository
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
    val repos: List<Repo> = emptyList()
) : MviState

sealed class SearchIntent : MviIntent {
    data class UpdateQuery(val query: String) : SearchIntent()
    object ToggleLoading : SearchIntent()
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val dataBaseDataSource: DataBaseDataSource,
    private val sharedPreferencesRepository: SharedPreferencesRepository
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
            dataBaseDataSource.clearRepos()
            dataBaseDataSource.insertRepos(result.items)
            setState { copy(isLoading = false, repos = result.items) }
        } else response.errorBody()?.let { error ->
            setState { copy(isLoading = false) }
            sendEvent(Event.LoadingError)
            Log.e(this::class.java.simpleName, error.string())
        }
    }

    private suspend fun loadLocalRepos() {
        dataBaseDataSource.getRepos().collect {
            it?.let { repos ->
                setState { copy(isLoading = false, repos = repos) }
            } ?: setState { copy(isLoading = false) }
        }
    }

    sealed class Event {
        object EmptyQuery : Event()
        object LoadingError : Event()
    }
}
