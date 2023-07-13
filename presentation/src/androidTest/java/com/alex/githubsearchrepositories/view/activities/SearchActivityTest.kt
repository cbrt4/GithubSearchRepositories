package com.alex.githubsearchrepositories.view.activities

import android.widget.EditText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import com.alex.githubsearchrepositories.search.SearchActivity
import com.alex.githubsearchrepositories.view.adapters.SearchRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class SearchActivityTest {
    @Rule
    @JvmField
    var rule: ActivityTestRule<SearchActivity> = ActivityTestRule(SearchActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun checkViews() {
        val activity = rule.activity
        assertThat(activity, notNullValue())
        assertThat(activity, instanceOf(SearchActivity::class.java))

        val editText = activity.searchEditText
        assertThat(editText, notNullValue())
        assertThat(editText, instanceOf(EditText::class.java))

        val recyclerView = activity.resultsRecyclerView
        assertThat(recyclerView, notNullValue())
        assertThat(recyclerView, instanceOf(androidx.recyclerview.widget.RecyclerView::class.java))

        val layoutManager = recyclerView.layoutManager
        assertThat(layoutManager, notNullValue())
        assertThat(
            layoutManager,
            instanceOf(androidx.recyclerview.widget.LinearLayoutManager::class.java)
        )

        val adapter = recyclerView.adapter
        assertThat(adapter, notNullValue())
        assertThat(adapter, instanceOf(SearchRecyclerAdapter::class.java))

        val floatingActionButton = activity.searchControlButton
        assertThat(floatingActionButton, notNullValue())
        assertThat(floatingActionButton, instanceOf(FloatingActionButton::class.java))
        assert(floatingActionButton.hasOnClickListeners())
    }
}