package com.alex.githubsearchrepositories.view.activities

import android.support.test.filters.MediumTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import com.alex.githubsearchrepositories.view.adapters.SearchRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@MediumTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField
    var rule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun checkActivity() {
        val activity = rule.activity
        assertThat(activity, notNullValue())
        assertThat(activity, instanceOf(MainActivity::class.java))
    }


    @Test
    @Throws(Exception::class)
    fun checkEditText() {
        val activity = rule.activity
        val editText = activity.searchEditText
        assertThat(editText, notNullValue())
        assertThat(editText, instanceOf(EditText::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun checkRecyclerView() {
        val activity = rule.activity
        val recyclerView = activity.resultsRecyclerView
        assertThat(recyclerView, notNullValue())
        assertThat(recyclerView, instanceOf(RecyclerView::class.java))

        val layoutManager = recyclerView.layoutManager
        assertThat(layoutManager, instanceOf(LinearLayoutManager::class.java))

        val adapter = recyclerView.adapter
        assertThat(adapter, instanceOf(SearchRecyclerAdapter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun checkButton() {
        val activity = rule.activity
        val floatingActionButton = activity.searchControlButton
        assertThat(floatingActionButton, notNullValue())
        assert(floatingActionButton.hasOnClickListeners())

    }
}