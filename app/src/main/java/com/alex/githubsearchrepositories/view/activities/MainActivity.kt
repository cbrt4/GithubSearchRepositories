package com.alex.githubsearchrepositories.view.activities

import android.os.Bundle
import android.widget.Toast
import com.alex.githubsearchrepositories.R
import com.alex.githubsearchrepositories.application.KitHubApplication
import com.alex.githubsearchrepositories.model.repo.RepoEntity
import com.alex.githubsearchrepositories.dagger.components.DaggerScreenComponent
import com.alex.githubsearchrepositories.presenters.MainPagePresenter
import com.alex.githubsearchrepositories.util.ScreenScope
import com.alex.githubsearchrepositories.view.activities.view.MainView
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@ScreenScope
class MainActivity : BaseActivity(), MainView {

    @Inject
    lateinit var mainPagePresenter: MainPagePresenter

    private var backPressedTimeOut: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)

        mainPagePresenter.view = this
        mainPagePresenter.loadRepos("calc")
    }

    override fun onPageLoaded(result: List<RepoEntity>) {
        for (repo in result) {
            mainText.append("$repo\n\n")
        }
    }

    override fun showLoading() {
        mainText.append("Loading...\n\n")
    }

    override fun hideLoading() {
        mainText.append("Loaded\n\n")
    }

    override fun showErrorMessage(error: String?) {
        mainText.text = error
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