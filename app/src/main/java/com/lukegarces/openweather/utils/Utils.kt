package com.lukegarces.openweather.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class Utils {
    object Utils {
        fun formatTime(timestamp: Long, timezone: Int): String {
            val date = Date((timestamp + timezone) * 1000)

            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")

            return sdf.format(date)
        }

        fun formatTime(dateText: String): String {
             val input = "2026-04-25 06:00:00"

            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM dd, yyyy hh:mm", Locale.getDefault())

            val date = inputFormat.parse(input)
            val formatted = outputFormat.format(date!!)

//            println(formatted) // Apr 25, 2026 06:00 AM = "2026-04-25 06:00:00"
//
//            val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//            val outputFormat = java.text.SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
//
//            val date = inputFormat.parse(input)
//            val formatted = outputFormat.format(date!!)
//
//            println(formatted) // Apr 25, 2026 06:00 AM
            
            return formatted
        }
    }
}
