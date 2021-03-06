package com.alex.githubsearchrepositories.util

import android.content.Context
import android.support.test.filters.SmallTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.alex.githubsearchrepositories.view.activities.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@SmallTest
@RunWith(AndroidJUnit4::class)
class SharedPreferencesManagerTest {

    @Rule
    @JvmField
    var rule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    private val fileName = "test_preferences"

    private val testFieldKey = "test_field_key"
    private val testFieldValue = "test_field_value"
    private val testFieldValue1 = "test_field_value_1"

    @Test
    fun getLastSearchQuery() {
        val sharedPreferences = rule.activity.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(testFieldKey, testFieldValue).apply()
        assert(sharedPreferences.getString(testFieldKey, "") == testFieldValue)
    }

    @Test
    fun setLastSearchQuery() {
        val sharedPreferences = rule.activity.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(testFieldKey, testFieldValue1).apply()
        assert(sharedPreferences.getString(testFieldKey, "") == testFieldValue1)
    }
}