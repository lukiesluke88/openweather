package com.lukegarces.openweather.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lukegarces.openweather.data.model.ApiResult
import com.lukegarces.openweather.viewmodel.WeatherViewModel

@Composable
fun WeatherListScreen(viewModel: WeatherViewModel) {

    val state by viewModel.weatherListState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWeatherList("manila")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val result = state) {
            is ApiResult.Loading -> {
                CircularProgressIndicator()
            }

            is ApiResult.Success -> {

                LazyColumn {
                    items(result.data.list) { item ->

                        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                            Text("Date: ${item.dt_txt}")
                            Text("Temp: ${item.main.temp}°C")
                            Text("Weather: ${item.weather.firstOrNull()?.description}")
                        }
                    }
                }
            }

            is ApiResult.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Error: ${result.message}",
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { viewModel.loadWeatherList("manila") }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}