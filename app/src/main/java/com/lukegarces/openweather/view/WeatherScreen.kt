package com.lukegarces.openweather.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.lukegarces.openweather.view.component.WeatherContent
import com.lukegarces.openweather.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val state by viewModel.weatherState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWeather(cityName = "manila")
    }

    Box(
        modifier = Modifier
            .fillMaxSize().padding(top = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
            when (state) {

                is ApiResult.Loading -> {
                    CircularProgressIndicator()
                }

                is ApiResult.Success -> {
                    val data = (state as ApiResult.Success).data
                    WeatherContent(data)
                }

                is ApiResult.Error -> {
                    val errorMessage = (state as ApiResult.Error).message

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.loadWeather("manila") }) {
                            Text("Retry")
                        }
                    }
            }
        }
    }
}
