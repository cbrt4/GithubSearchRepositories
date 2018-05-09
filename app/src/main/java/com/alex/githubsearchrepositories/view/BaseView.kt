package com.alex.githubsearchrepositories.view

interface BaseView {

    fun showLoading()

    fun hideLoading()

    fun showErrorMessage(error: String?)
}