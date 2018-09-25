package com.alex.githubsearchrepositories.presenters

import android.util.Log
import com.alex.githubsearchrepositories.model.repo.RepoEntity
import com.alex.githubsearchrepositories.model.room.dao.RepoDao
import com.alex.githubsearchrepositories.network.ApiRequestService
import com.alex.githubsearchrepositories.util.ScreenScope
import com.alex.githubsearchrepositories.util.SharedPreferencesManager
import com.alex.githubsearchrepositories.view.activities.view.MainView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@ScreenScope
class MainPagePresenter @Inject constructor(private val apiRequestService: ApiRequestService,
                                            private val repoDao: RepoDao,
                                            private val sharedPreferencesManager: SharedPreferencesManager) : BasePresenter<MainView>() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var currentQuery = ""

    fun loadRepos(searchQuery: String) {
        view?.showLoading()
        currentQuery = searchQuery
        loadQuery()
    }

    private fun getRepos(searchQuery: String) {
        compositeDisposable.add(apiRequestService.searchRepos(searchQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    saveRepos(it.items)
                    saveQuery(searchQuery)
                    view?.onPageLoaded(it.items)
                    view?.hideLoading()
                }, {
                    view?.hideLoading()
                    view?.showErrorMessage(it.localizedMessage)
                    Log.e("Load", "Error", it)
                }))
    }

    private fun getReposFromDb() {
        compositeDisposable.add(repoDao.getRepos().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.onPageLoaded(it as ArrayList<RepoEntity>)
                    view?.hideLoading()
                }, {
                    view?.hideLoading()
                    view?.showErrorMessage(it.localizedMessage)
                    Log.e("Load", "Error", it)
                }))
    }

    private fun saveRepos(repos: ArrayList<RepoEntity>) {
        compositeDisposable.add(Observable.fromCallable { repoDao.clearRepos() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e("Clear", "Success")

                    compositeDisposable.add(Observable.fromCallable { repoDao.insertRepos(repos) }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Log.e("Save", "Success")
                            }, {
                                Log.e("Save", "Error", it)
                            }))

                }, {
                    Log.e("Clear", "Error", it)
                }))
    }

    private fun saveQuery(query: String) {
        compositeDisposable.add(Observable.fromCallable { sharedPreferencesManager.setLastSearchQuery(query) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e("Save", "Success")
                }, {
                    Log.e("Save", "Error", it)
                }))
    }

    private fun loadQuery() {
        compositeDisposable.add(sharedPreferencesManager.getLastSearchQuery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it == currentQuery) {
                        getReposFromDb()
                    } else {
                        getRepos(currentQuery)
                    }
                }, {
                    Log.e("Load", "Error", it)
                }))
    }

    override fun cancel() {
        view?.hideLoading()
        compositeDisposable.clear()
    }

    override fun destroy() {
        view = null
        compositeDisposable.dispose()
    }
}
