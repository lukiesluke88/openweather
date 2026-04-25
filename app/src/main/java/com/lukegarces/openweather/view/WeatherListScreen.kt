package com.lukegarces.openweather.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lukegarces.openweather.data.model.ApiResult
import com.lukegarces.openweather.utils.Utils.Utils.formatTime
import com.lukegarces.openweather.viewmodel.WeatherViewModel

@Composable
fun WeatherListScreen(viewModel: WeatherViewModel) {
    val state by viewModel.weatherListState.collectAsState()

    val listState = rememberSaveable(
        saver = LazyListState.Saver
    ) {
        LazyListState()
    }
    LaunchedEffect(Unit) {
        viewModel.loadWeatherList("manila")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val result = state) {
            is ApiResult.Loading -> {
                CircularProgressIndicator()
            }

            is ApiResult.Success -> {
                LazyColumn(state = listState) {
                    items(result.data.list) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(top = 4.dp), horizontalAlignment = Alignment.End
                                ) {
                                    Text(formatTime(item.dt_txt), fontWeight = FontWeight.Bold)
                                    Text("${item.main.temp}°C")
                                    Text(item.weather.firstOrNull()?.description ?: "")
                                }

                                AsyncImage(
                                    model = "https://openweathermap.org/img/wn/${item.weather.firstOrNull()?.icon ?: ""}@2x.png",
                                    contentDescription = "Weather Icon",
                                    modifier = Modifier.size(120.dp)
                                )
                            }
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