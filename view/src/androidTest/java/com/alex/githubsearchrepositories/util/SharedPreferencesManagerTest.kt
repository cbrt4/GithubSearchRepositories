package com.alex.githubsearchrepositories.util

import android.content.Context
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.alex.githubsearchrepositories.search.SearchActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@SmallTest
@RunWith(AndroidJUnit4::class)
class SharedPreferencesManagerTest {

    @Rule
    @JvmField
    var rule: ActivityScenarioRule<SearchActivity> =
        ActivityScenarioRule(SearchActivity::class.java)

    private val fileName = "test_preferences"

    private val testFieldKey = "test_field_key"
    private val testFieldValue = "test_field_value"
    private val testFieldValue1 = "test_field_value_1"

    @Test
    fun getLastSearchQuery() {
        rule.scenario.onActivity {
            val sharedPreferences = it.getSharedPreferences(fileName, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(testFieldKey, testFieldValue).apply()
            assert(sharedPreferences.getString(testFieldKey, "") == testFieldValue)
        }
    }

    @Test
    fun setLastSearchQuery() {
        rule.scenario.onActivity {
            val sharedPreferences = it.getSharedPreferences(fileName, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(testFieldKey, testFieldValue1).apply()
            assert(sharedPreferences.getString(testFieldKey, "") == testFieldValue1)
        }
    }
}