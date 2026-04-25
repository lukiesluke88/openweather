package com.lukegarces.openweather.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector
import com.lukegarces.openweather.data.model.WeatherResponse
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

        fun getWeatherIcon(weather: WeatherResponse): ImageVector {
            val isNight = System.currentTimeMillis() / 1000 > weather.sys.sunset

            return when (weather.weather.firstOrNull()?.main) {
                "Clear" -> if (isNight) Icons.Default.Nightlight else Icons.Default.WbSunny
                "Clouds" -> Icons.Default.Cloud
                "Rain" -> Icons.Default.Umbrella
                else -> Icons.Default.Cloud
            }
        }
    }
}