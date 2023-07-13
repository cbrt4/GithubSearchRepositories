package com.alex.githubsearchrepositories.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import com.alex.githubsearchrepositories.R
import com.alex.githubsearchrepositories.databinding.ActivityMainBinding
import com.alex.githubsearchrepositories.view.activities.AbstractActivity
import com.alex.githubsearchrepositories.view.adapters.SearchRecyclerAdapter
import com.example.model.dto.repo.Repo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AbstractActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: SearchViewModel by viewModels()

    private val searchRecyclerAdapter = SearchRecyclerAdapter()

    private var isLoading = false
    private var backPressedTimeOut: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.collectState {
            when (it) {
                SearchViewModel.State.Idle -> hideLoading()
                SearchViewModel.State.Loading -> showLoading()
                is SearchViewModel.State.Success -> {
                    hideLoading()
                    update(it.data)
                }

                is SearchViewModel.State.Error -> {
                    hideLoading()
                    reportError(it.message)
                }
            }
        }

        setupViews()
    }

    private fun setupViews() {
        setupRecyclerView()
        setupSearchButton()
    }

    private fun setupRecyclerView() = with(binding) {
        resultsRecyclerView.adapter = searchRecyclerAdapter
    }

    private fun setupSearchButton() = with(binding) {
        searchControlButton.setOnClickListener {
            loadSearchResults()
            hideKeyboard()
        }
    }

    private fun loadSearchResults() = with(binding) {
        if (!isLoading) {
            val currentSearchQuery = searchEditText.text.toString()
            if (currentSearchQuery.isEmpty()) {
                showToast(getString(R.string.emty_query))
                return
            }
            searchRecyclerAdapter.searchResults = ArrayList()
            searchRecyclerAdapter.notifyDataSetChanged()
            viewModel.dispatchIntent(SearchViewModel.Intent.Load(currentSearchQuery))
        } else {
            viewModel.dispatchIntent(SearchViewModel.Intent.Cancel)
        }
    }

    private fun update(element: List<Repo>) {
        searchRecyclerAdapter.searchResults = element
        searchRecyclerAdapter.notifyDataSetChanged()
    }

    private fun showLoading() = with(binding) {
        errorTextView.visibility = View.GONE
        searchControlButton.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_cancel,
                this@SearchActivity.theme
            )
        )
        loadingProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideLoading() = with(binding) {
        searchControlButton.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_confirm,
                this@SearchActivity.theme
            )
        )
        loadingProgressBar.visibility = View.GONE
        isLoading = false
    }

    private fun reportError(errorMessage: String) = with(binding) {
        searchRecyclerAdapter.searchResults = ArrayList()
        errorTextView.visibility = View.VISIBLE
        errorTextView.text = errorMessage
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onBackPressed() {
        if (backPressedTimeOut + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, R.string.on_back_pressed, Toast.LENGTH_SHORT).show()
            backPressedTimeOut = System.currentTimeMillis()
        }
    }
}
