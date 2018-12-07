package com.alex.githubsearchrepositories.presenters

abstract class Presenter<V> {

    var view: V? = null

    abstract fun cancel()

    abstract fun destroy()
}