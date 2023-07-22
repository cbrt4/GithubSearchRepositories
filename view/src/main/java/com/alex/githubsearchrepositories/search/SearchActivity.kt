package com.alex.githubsearchrepositories.search

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.alex.githubsearchrepositories.R
import com.alex.githubsearchrepositories.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<SearchViewModel>()

    private val searchRecyclerAdapter = SearchRecyclerAdapter()

    private var backPressedTimeOut: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.collectState(::updateUiState)
        viewModel.collectEvent {
            if (it is SearchViewModel.Event) when (it) {
                SearchViewModel.Event.EmptyQuery -> showToast(getString(R.string.empty_query))
                SearchViewModel.Event.LoadingError -> showToast(getString(R.string.loading_error))
            }
        }

        setupViews()
    }

    private fun setupViews() {
        setupEdiText()
        setupRecyclerView()
        setupSearchButton()
    }

    private fun setupEdiText() = binding.searchEditText.addTextChangedListener {
        viewModel.dispatchIntent(SearchIntent.UpdateQuery(it.toString()))
    }

    private fun setupRecyclerView() {
        binding.resultsRecyclerView.adapter = searchRecyclerAdapter
    }


    private fun setupSearchButton() = binding.searchControlButton.setOnClickListener {
        viewModel.dispatchIntent(SearchIntent.ToggleLoading)
        hideKeyboard()
    }

    private fun updateUiState(state: SearchViewState) = with(binding) {
        searchRecyclerAdapter.updateData(state.repos)
        resultsRecyclerView.isVisible = !state.isLoading
        loadingProgressBar.isVisible = state.isLoading
        searchControlButton.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                if (state.isLoading) R.drawable.ic_cancel else R.drawable.ic_confirm,
                this@SearchActivity.theme
            )
        )
        // TODO: errorTextView.isVisible = state.isLoading???
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    private fun hideKeyboard() {
        this.currentFocus?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
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
