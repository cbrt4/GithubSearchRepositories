package com.alex.githubsearchrepositories.presenters

import com.alex.githubsearchrepositories.model.repo.RepoEntity
import com.alex.githubsearchrepositories.model.room.dao.RepoDao
import com.alex.githubsearchrepositories.model.search.SearchResponseEntity
import com.alex.githubsearchrepositories.network.ApiRequestService
import com.alex.githubsearchrepositories.view.activities.view.MainView
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MainPagePresenterTest {

    @Rule
    var scheduler = TestScheduler()

    @Mock
    lateinit var mainView: MainView

    @Mock
    lateinit var apiRequestService: ApiRequestService

    @Mock
    lateinit var repoDao: RepoDao

    lateinit var mainPagePresenter: MainPagePresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mainPagePresenter = MainPagePresenter(apiRequestService, repoDao)
        mainPagePresenter.view = mainView
    }

    @Test
    @Throws(Exception::class)
    fun testResponse() {
        val result = arrayListOf(RepoEntity(null, "Test", "Test", "Test", "Test", 0))
        val response = SearchResponseEntity(1, false, result)
        `when`(apiRequestService.searchRepos("")).thenReturn(Observable.just<SearchResponseEntity>(response))
        mainPagePresenter.loadRepos("", false)
        verify(mainView, times(1))?.onPageLoaded(result)
    }

    @Test
    @Throws(Exception::class)
    fun testError() {
        val result = arrayListOf(RepoEntity(null, "Test", "Test", "Test", "Test", 0))
        val response = SearchResponseEntity(1, false, result)
        `when`(apiRequestService.searchRepos("")).thenReturn(Observable.just<SearchResponseEntity>(response))
        mainPagePresenter.loadRepos("", false)
        verify(mainView, times(1))?.showErrorMessage("Error")
    }

}