package com.alex.githubsearchrepositories.presenters

abstract class AbstractPresenter<V> {

    var view: V? = null

    abstract fun cancel()

    abstract fun destroy()
}