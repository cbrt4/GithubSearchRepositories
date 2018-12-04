package com.alex.githubsearchrepositories.presenters

import android.arch.lifecycle.Observer
import android.util.Log
import com.alex.githubsearchrepositories.model.repo.RepoEntity
import com.alex.githubsearchrepositories.model.room.dao.RepoDao
import com.alex.githubsearchrepositories.network.ApiRequestService
import com.alex.githubsearchrepositories.util.ScreenScope
import com.alex.githubsearchrepositories.util.SharedPreferencesManager
import com.alex.githubsearchrepositories.view.AbstractView
import com.alex.githubsearchrepositories.view.activities.MainActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.android.Main
import javax.inject.Inject

@ScreenScope
class MainPagePresenter @Inject constructor(private val apiRequestService: ApiRequestService,
                                            private val repoDao: RepoDao,
                                            private val sharedPreferencesManager: SharedPreferencesManager) : AbstractPresenter<AbstractView<List<RepoEntity>>>() {

    private var currentQuery = ""
    private var currentJob: Job? = null

    fun loadRepos(searchQuery: String) {
        view?.showLoading()
        currentQuery = searchQuery
        loadQuery()
    }

    private fun getRepos(searchQuery: String) {
        currentJob = GlobalScope.launch(Dispatchers.Main) {
            view?.showLoading()
            val response = apiRequestService.searchRepos(searchQuery).await()
            view?.hideLoading()
            if (response.isSuccessful) {
                response.body()?.let {
                    saveRepos(it.items)
                    saveQuery(searchQuery)
                    view?.update(it.items)
                }
            } else {
                response.errorBody()?.let {
                    view?.reportError(it.string())
                    Log.e("MainPresenter", it.string())
                }
            }
        }
    }

    private fun getReposFromDb() {
        GlobalScope.launch(
                Dispatchers.Default,
                CoroutineStart.DEFAULT,
                { throwable ->
                    throwable?.let {
                        Log.e("MainPresenter", it.localizedMessage)
                    }
                },
                {
                    repoDao.getRepos().observe(view as MainActivity, Observer<List<RepoEntity>> { repos ->
                        view?.hideLoading()
                        repos?.let {
                            view?.update(it)
                        }
                    })
                })
    }

    private fun saveRepos(repos: ArrayList<RepoEntity>) {
        GlobalScope.launch(Dispatchers.Default,
                CoroutineStart.DEFAULT,
                { repoDao.insertRepos(repos) },
                { repoDao.clearRepos() })
    }

    private fun saveQuery(query: String) {
        GlobalScope.launch(Dispatchers.Default,
                CoroutineStart.DEFAULT,
                { throwable ->
                    throwable?.let {
                        Log.e("MainPresenter", it.localizedMessage)
                    }
                },
                { sharedPreferencesManager.setLastSearchQuery(query) })
    }

    private fun loadQuery() {
        GlobalScope.launch(Dispatchers.Default,
                CoroutineStart.DEFAULT,
                { throwable ->
                    throwable?.let {
                        Log.e("", it.localizedMessage)
                    }
                },
                {
                    val previousQuery = sharedPreferencesManager.getLastSearchQuery()
                    if (previousQuery == currentQuery) {
                        getReposFromDb()
                    } else {
                        getRepos(currentQuery)
                    }
                })
    }

    override fun cancel() {
        currentJob?.cancel()
        view?.hideLoading()
    }

    override fun destroy() {
        view = null
    }
}
