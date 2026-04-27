package com.lukegarces.openweather.view

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lukegarces.openweather.data.model.ApiResult
import com.lukegarces.openweather.view.component.WeatherContent
import com.lukegarces.openweather.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val weatherState by viewModel.weatherState.collectAsState()

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val fineGranted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

            val coarseGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (fineGranted || coarseGranted) {
                viewModel.loadWeatherByCurrentLocation()
            }
        }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
//        viewModel.loadWeather(cityName = "manila")
    }
    val cachedWeather by viewModel.cachedWeather.collectAsState(initial = null)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 2.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        when (val state = weatherState) {

            is ApiResult.Loading -> {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Magenta.copy(alpha = 0.5f)
                )
                cachedWeather?.let {
                    WeatherContent(it)
                }
            }

            is ApiResult.Success -> {
                val data = state.data
                WeatherContent(data)
            }

            is ApiResult.Error -> {
                val errorMessage = (weatherState as ApiResult.Error).message

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { viewModel.loadWeatherByCurrentLocation() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}
