package com.alex.githubsearchrepositories.view.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.*
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Locale.setDefault(Locale.US)

        inject()
    }

    abstract fun inject()

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
    }

    fun hideKeyboard() {
        this.currentFocus?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}