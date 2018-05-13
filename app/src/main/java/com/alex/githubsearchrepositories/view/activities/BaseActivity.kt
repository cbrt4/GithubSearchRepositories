package com.alex.githubsearchrepositories.view.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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

    fun showToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun hideKeyboard() {
        this.currentFocus?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}