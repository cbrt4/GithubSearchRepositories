package com.alex.githubsearchrepositories.view.activities.view

import com.alex.githubsearchrepositories.model.repo.RepoEntity
import com.alex.githubsearchrepositories.view.BaseView

interface MainView : BaseView {
    fun onPageLoaded(result: ArrayList<RepoEntity>)
}