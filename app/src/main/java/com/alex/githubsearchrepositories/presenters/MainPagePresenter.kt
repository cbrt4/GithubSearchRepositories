package com.alex.githubsearchrepositories.presenters

import android.util.Log
import com.alex.githubsearchrepositories.model.repo.RepoEntity
import com.alex.githubsearchrepositories.model.room.dao.RepoDao
import com.alex.githubsearchrepositories.model.search.SearchResponseEntity
import com.alex.githubsearchrepositories.network.ApiRequestService
import com.alex.githubsearchrepositories.util.ScreenScope
import com.alex.githubsearchrepositories.view.activities.view.MainView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@ScreenScope
class MainPagePresenter @Inject constructor(private val apiRequestService: ApiRequestService,
                                            private val repoDao: RepoDao) : BasePresenter<MainView>() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun loadRepos(searchQuery: String, loadFromCache: Boolean) {

        view?.showLoading()

        if (loadFromCache) {
            getReposFromDb()
        } else {
            apiRequestService.searchRepos(searchQuery)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            object : Observer<SearchResponseEntity> {
                                override fun onSubscribe(disposable: Disposable) {
                                    compositeDisposable.add(disposable)
                                }

                                override fun onNext(response: SearchResponseEntity) {
                                    response.let {
                                        if (it.items.size > 0) {
                                            saveRepos(it.items)
                                            view?.onPageLoaded(it.items)
                                        } else {
                                            view?.showErrorMessage("No results for request '$searchQuery'")
                                        }
                                    }
                                }

                                override fun onComplete() {
                                    view?.hideLoading()
                                }

                                override fun onError(e: Throwable) {
                                    view?.hideLoading()
                                    view?.showErrorMessage(e.localizedMessage)
                                }
                            }
                    )
        }
    }

    private fun getReposFromDb() {
        compositeDisposable.add(repoDao.getRepos().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    view?.onPageLoaded(it as ArrayList<RepoEntity>)
                }, {
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

    override fun cancel() {
        view?.hideLoading()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
            compositeDisposable = CompositeDisposable()
        }
    }

    override fun destroy() {
        view = null
    }
}
