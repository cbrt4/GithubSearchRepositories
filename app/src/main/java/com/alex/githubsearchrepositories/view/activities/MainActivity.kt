package com.alex.githubsearchrepositories.view.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.alex.githubsearchrepositories.R
import com.alex.githubsearchrepositories.application.KitHubApplication
import com.alex.githubsearchrepositories.dagger.components.DaggerScreenComponent
import com.alex.githubsearchrepositories.model.repo.RepoEntity
import com.alex.githubsearchrepositories.presenters.MainPagePresenter
import com.alex.githubsearchrepositories.util.ScreenScope
import com.alex.githubsearchrepositories.util.SharedPreferencesManager
import com.alex.githubsearchrepositories.view.activities.view.MainView
import com.alex.githubsearchrepositories.view.adapters.SearchRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@ScreenScope
class MainActivity : BaseActivity(), MainView {

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    @Inject
    lateinit var mainPagePresenter: MainPagePresenter

    @Inject
    lateinit var searchRecyclerAdapter: SearchRecyclerAdapter

    private var lastSearchQuery = ""
    private var currentSearchQuery = ""
    private var isLoading = false
    private var backPressedTimeOut: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)

        setupViews()

        lastSearchQuery = sharedPreferencesManager.getLastSearchQuery()
        mainPagePresenter.view = this
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPagePresenter.destroy()
    }

    private fun setupViews() {
        setupRecyclerView()
        setupSearchButton()
    }

    private fun setupRecyclerView() {
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)
        resultsRecyclerView.adapter = searchRecyclerAdapter
    }

    private fun setupSearchButton() {
        searchControlButton.setOnClickListener {
            if (!isLoading) {
                currentSearchQuery = searchEditText.text.toString()
                if (currentSearchQuery.isEmpty()) {
                    showToast(getString(R.string.emty_query))
                    return@setOnClickListener
                }
                val isCached = currentSearchQuery == lastSearchQuery
                mainPagePresenter.loadRepos(currentSearchQuery, isCached)
            } else {
                mainPagePresenter.cancel()
            }
            hideKeyboard()
        }
    }

    private fun saveQuery(query: String) {
        lastSearchQuery = query
        sharedPreferencesManager.setLastSearchQuery(query)
    }

    override fun onPageLoaded(result: ArrayList<RepoEntity>) {
        searchRecyclerAdapter.searchResults = result
        searchRecyclerAdapter.notifyDataSetChanged()
        saveQuery(currentSearchQuery)
    }

    override fun showLoading() {
        searchControlButton.setImageDrawable(resources.getDrawable(R.drawable.ic_cancel, this.theme))
        errorTextView.visibility = View.GONE
        resultsRecyclerView.visibility = View.GONE
        loadingProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    override fun hideLoading() {
        searchControlButton.setImageDrawable(resources.getDrawable(R.drawable.ic_confirm, this.theme))
        resultsRecyclerView.visibility = View.VISIBLE
        loadingProgressBar.visibility = View.GONE
        isLoading = false
    }

    override fun showErrorMessage(error: String?) {
        searchRecyclerAdapter.searchResults.clear()
        errorTextView.visibility = View.VISIBLE
        errorTextView.text = error
    }

    override fun inject() = DaggerScreenComponent.builder()
            .applicationComponent((application as KitHubApplication).applicationComponent)
            .build().inject(this)

    override fun onBackPressed() {
        if (backPressedTimeOut + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, R.string.on_back_pressed, Toast.LENGTH_SHORT).show()
            backPressedTimeOut = System.currentTimeMillis()
        }
    }
}