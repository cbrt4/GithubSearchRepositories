package com.alex.githubsearchrepositories.view.activities

import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.alex.githubsearchrepositories.search.SearchActivity
import com.alex.githubsearchrepositories.search.SearchRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class SearchActivityTest {
    @Rule
    @JvmField
    var rule: ActivityScenarioRule<SearchActivity> =
        ActivityScenarioRule(SearchActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun checkViews() {
        rule.scenario.onActivity { activity ->
            assertThat(activity, notNullValue())
            assertThat(activity, instanceOf(SearchActivity::class.java))

            val editText = activity.searchEditText
            assertThat(editText, notNullValue())
            assertThat(editText, instanceOf(EditText::class.java))

            val recyclerView = activity.resultsRecyclerView
            assertThat(recyclerView, notNullValue())
            assertThat(
                recyclerView,
                instanceOf(RecyclerView::class.java)
            )

            val layoutManager = recyclerView.layoutManager
            assertThat(layoutManager, notNullValue())
            assertThat(
                layoutManager,
                instanceOf(LinearLayoutManager::class.java)
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
}