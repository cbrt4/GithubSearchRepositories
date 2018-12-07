package com.alex.githubsearchrepositories.view.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.alex.githubsearchrepositories.R
import com.alex.githubsearchrepositories.model.repo.Repo
import com.alex.githubsearchrepositories.presenters.MainPagePresenter
import com.alex.githubsearchrepositories.util.Layout
import com.alex.githubsearchrepositories.view.AbstractView
import com.alex.githubsearchrepositories.view.adapters.SearchRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*

@Layout(id = R.layout.activity_main)
class MainActivity : AbstractActivity(), AbstractView<List<Repo>> {

    private val mainPagePresenter = MainPagePresenter()
    private val searchRecyclerAdapter = SearchRecyclerAdapter()

    private var isLoading = false
    private var backPressedTimeOut: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
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
            loadSearchResults()
            hideKeyboard()
        }
    }

    private fun loadSearchResults() {
        if (!isLoading) {
            val currentSearchQuery = searchEditText.text.toString()
            if (currentSearchQuery.isEmpty()) {
                showToast(getString(R.string.emty_query))
                return
            }
            searchRecyclerAdapter.searchResults = ArrayList()
            searchRecyclerAdapter.notifyDataSetChanged()
            mainPagePresenter.loadRepos(currentSearchQuery)
        } else {
            mainPagePresenter.cancel()
        }
    }

    override fun update(element: List<Repo>) {
        searchRecyclerAdapter.searchResults = element
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

    override fun reportError(errorMessage: String) {
        searchRecyclerAdapter.searchResults = ArrayList()
        errorTextView.visibility = View.VISIBLE
        errorTextView.text = errorMessage
    }

    override fun onBackPressed() {
        if (backPressedTimeOut + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, R.string.on_back_pressed, Toast.LENGTH_SHORT).show()
            backPressedTimeOut = System.currentTimeMillis()
        }
    }
}