package com.alex.githubsearchrepositories.view.activities

import android.content.Context
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class AbstractActivity : AppCompatActivity() {

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