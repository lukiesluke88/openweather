package com.lukegarces.openweather.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class Utils {
    object Utils {

        fun formatTime(timestamp: Long, timezoneOffset: Int): String {
            val date = Date(timestamp * 1000)

            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())

            val hours = timezoneOffset / 3600
            val minutes = (timezoneOffset % 3600) / 60

            val tzId = String.format("GMT%+d:%02d", hours, minutes)
            sdf.timeZone = TimeZone.getTimeZone(tzId)

            return sdf.format(date)
        }

        fun formatTimeStandard(dateText: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())

            val date = inputFormat.parse(dateText)
            val formatted = outputFormat.format(date!!)

            return formatted
        }
    }
}
