package com.alex.githubsearchrepositories.presenters

abstract class BasePresenter<T> {

    var view: T? = null

    abstract fun cancel()

    abstract fun destroy()
}