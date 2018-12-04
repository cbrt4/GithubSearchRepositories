package com.alex.githubsearchrepositories.view

interface AbstractView<E> {

    fun showLoading()

    fun hideLoading()

    fun update(element: E)

    fun reportError(errorMessage: String)
}