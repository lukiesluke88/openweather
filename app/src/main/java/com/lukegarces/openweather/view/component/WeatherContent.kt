package com.lukegarces.openweather.view.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lukegarces.openweather.data.model.WeatherResponse
import com.lukegarces.openweather.utils.Utils.Utils.formatTime

@Composable
fun WeatherContent(weather: WeatherResponse?) {

    if (weather == null) return

    val sunrise = formatTime(weather.sys.sunrise, weather.timezone)
    val sunset = formatTime(weather.sys.sunset, weather.timezone)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        val iconCode = weather.weather.firstOrNull()?.icon ?: ""

        AsyncImage(
            model = "https://openweathermap.org/img/wn/${iconCode}@2x.png",
            contentDescription = "Weather Icon",
            modifier = Modifier.size(200.dp)
        )

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "${weather.name}, ${weather.sys.country}",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${weather.main.temp.toInt()} °C",
                style = MaterialTheme.typography.displaySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Sunrise: $sunrise")
            Text(text = "Sunset: $sunset")
        }
    }
}
