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
import retrofit2.Response
import javax.inject.Inject

@ScreenScope
class MainPagePresenter @Inject constructor(private val apiRequestService: ApiRequestService,
                                            private val repoDao: RepoDao) : BasePresenter<MainView>() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun loadRepos(searchQuery: String) {

        view?.showLoading()

        apiRequestService.searchRepos(searchQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        object : Observer<Response<SearchResponseEntity>> {
                            override fun onSubscribe(disposable: Disposable) {
                                compositeDisposable.add(disposable)
                            }

                            override fun onNext(response: Response<SearchResponseEntity>) {
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        saveRepos(it.items)
                                        //view?.onPageLoaded(it.items)
                                    }
                                } else view?.showErrorMessage("Error code " + response.code())
                            }

                            override fun onComplete() {
                                view?.hideLoading()
                            }

                            override fun onError(e: Throwable) {
                                view?.showErrorMessage(e.localizedMessage)
                            }
                        }
                )
    }

    fun getReposFromDb() {
        compositeDisposable.add(repoDao.getAllTasks().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.onPageLoaded(it)
                }, {
                    Log.e("Load", "loadTasks Error", it)
                }))
    }

    private fun saveRepos(repos: List<RepoEntity>) {
        compositeDisposable.add(Observable.fromCallable { repoDao.insertRepos(repos) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //view?.onSuccessAddTask()
                    getReposFromDb()
                    Log.e("Save", "addNewTask Success")
                }, {
                    Log.e("Save", "addNewTask Error", it)
                }))
    }

    override fun cancel() {
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
    }

    override fun destroy() {
        view = null
    }
}
