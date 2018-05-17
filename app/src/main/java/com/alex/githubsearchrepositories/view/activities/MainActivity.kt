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
import com.alex.githubsearchrepositories.view.activities.view.MainView
import com.alex.githubsearchrepositories.view.adapters.SearchRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@ScreenScope
class MainActivity : BaseActivity(), MainView {

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
                searchRecyclerAdapter.searchResults.clear()
                searchRecyclerAdapter.notifyDataSetChanged()
                mainPagePresenter.loadRepos(currentSearchQuery)
            } else {
                mainPagePresenter.cancel()
            }
            hideKeyboard()
        }
    }

    override fun onPageLoaded(result: ArrayList<RepoEntity>) {
        searchRecyclerAdapter.searchResults = result
        searchRecyclerAdapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        errorTextView.visibility = View.GONE
        searchControlButton.setImageDrawable(resources.getDrawable(R.drawable.ic_cancel, this.theme))
        loadingProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    override fun hideLoading() {
        searchControlButton.setImageDrawable(resources.getDrawable(R.drawable.ic_confirm, this.theme))
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