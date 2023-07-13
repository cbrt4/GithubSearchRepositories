package com.alex.githubsearchrepositories.search

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.model.database.DataBaseDataSource
import com.example.model.dto.repo.Repo
import com.example.model.network.NetworkDataSource
import com.example.model.sharedpreferences.SharedPreferencesDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import inc.alex.mvi.MviViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val networkDataSource: NetworkDataSource,
    private val dataBaseDataSource: DataBaseDataSource,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) : MviViewModel<SearchViewModel.Intent, SearchViewModel.State>() {

    private var currentJob: Job? = null
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        Log.e(this::class.java.simpleName, "${exception.message}")
    }

    override val state = MutableStateFlow<State>(State.Idle)

    override fun dispatchIntent(intent: Intent) {
        when (intent) {
            is Intent.Load -> {
                state.value = State.Loading
                loadQuery(intent.query)
            }

            Intent.Cancel -> {
                state.value = State.Idle
                currentJob?.cancel()
                currentJob = null
            }
        }
    }

    private fun loadQuery(query: String) {
        println(">>> loadQuery($query)")
        currentJob = viewModelScope.launch(errorHandler) {
            val previousQuery = sharedPreferencesDataSource.getLastSearchQuery()
            println(">>> $previousQuery, $query")
            if (previousQuery == query) {
                loadLocalRepos()
            } else {
                loadRemoteRepos(query)
            }
        }
    }

    private suspend fun loadRemoteRepos(searchQuery: String) {
        val response = networkDataSource.loadRepos(searchQuery)
        if (response.isSuccessful) response.body()?.let {
            if (it.items.isEmpty()) {
                state.value = State.Error("No results for request '$searchQuery'")
            } else {
                saveRepos(it.items)
                saveQuery(searchQuery)
                state.value = State.Success(it.items)
            }
        } else response.errorBody()?.let {
            state.value = State.Error(it.string())
            Log.e(this::class.java.simpleName, it.string())
        }
    }

    private suspend fun loadLocalRepos() {
        dataBaseDataSource.getRepos().collect { repos ->
            repos?.let {
                state.value = State.Success(it)
            }
        }
    }

    private fun saveRepos(repos: List<Repo>) {
        viewModelScope.launch(errorHandler) {
            dataBaseDataSource.insertRepos(repos)
        }
    }

    private fun saveQuery(query: String) {
        viewModelScope.launch(errorHandler) {
            sharedPreferencesDataSource.setLastSearchQuery(query)
        }
    }

    sealed class State {
        object Idle : State()
        object Loading : State()
        data class Success(val data: List<Repo> = emptyList()) : State()
        data class Error(val message: String) : State()
    }

    sealed class Intent {
        data class Load(val query: String) : Intent()
        object Cancel : Intent()
    }
}
