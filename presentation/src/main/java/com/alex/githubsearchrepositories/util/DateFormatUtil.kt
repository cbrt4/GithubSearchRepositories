package com.alex.githubsearchrepositories.util

import android.text.format.DateFormat
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateFormatUtil {

    companion object {
        private const val FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        private const val TAG = "DateFormatUtil"

        private const val SECOND: Long = 1000
        private const val MINUTE = 60 * SECOND
        private const val HOUR = 60 * MINUTE
        private const val DAY = 24 * HOUR

        fun getTime(date: String?): String {
            Locale.setDefault(Locale.US)

            val format = SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault())
            val dateMillis: Long
            val difference: Long
            val minutes: Long
            val hours: Long

            dateMillis = try {
                format.parse(date).time
            } catch (e: Exception) {
                Log.e(TAG, e.localizedMessage)
                System.currentTimeMillis()
            }

            difference = System.currentTimeMillis() - dateMillis
            return if (difference < SECOND) {
                "just now"
            } else if (difference < MINUTE) {
                "last minute"
            } else if (difference in MINUTE..(HOUR - 1)) {
                minutes = difference / MINUTE
                if (minutes == 1L) {
                    "a minute ago"
                } else {
                    "$minutes minutes ago"
                }
            } else if (difference in HOUR..(DAY - 1)) {
                hours = difference / HOUR
                if (hours == 1L) {
                    "an hour ago"
                } else {
                    "$hours hours ago"
                }
            } else if (difference >= DAY && difference < 2 * DAY) {
                "yesterday"
            } else {
                DateFormat.format("MMM d, yyyy", Date(dateMillis)).toString()
            }
        }
    }
}