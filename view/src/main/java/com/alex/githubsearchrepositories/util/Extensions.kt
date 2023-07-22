package com.alex.githubsearchrepositories.util

import android.content.Context
import android.text.format.DateFormat
import com.alex.githubsearchrepositories.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String?.getTime(context: Context): String {
    Locale.setDefault(Locale.US)

    val format = SimpleDateFormat(INPUT_PATTERN, Locale.getDefault())

    val dateMillis: Long = this?.let {
        format.parse(it)?.time ?: System.currentTimeMillis()
    } ?: System.currentTimeMillis()

    val difference = System.currentTimeMillis() - dateMillis
    return when {
        difference < SECOND -> {
            context.getString(R.string.just_now)
        }

        difference < MINUTE -> {
            context.getString(R.string.last_minute)
        }

        difference in MINUTE until HOUR -> {
            val minutes = difference / MINUTE
            if (minutes == 1L) {
                context.getString(R.string.a_minute_ago)
            } else {
                context.getString(R.string.minutes_ago, minutes)
            }
        }

        difference in HOUR until DAY -> {
            val hours = difference / HOUR
            if (hours == 1L) {
                context.getString(R.string.an_hour_ago)
            } else {
                context.getString(R.string.hours_ago, hours)
            }
        }

        difference < 2 * DAY -> {
            context.getString(R.string.yesterday)
        }

        else -> {
            DateFormat.format(OUTPUT_PATTERN, Date(dateMillis)).toString()
        }
    }
}

private const val INPUT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
private const val OUTPUT_PATTERN = "MMM d, yyyy"

private const val SECOND: Long = 1000
private const val MINUTE = 60 * SECOND
private const val HOUR = 60 * MINUTE
private const val DAY = 24 * HOUR
