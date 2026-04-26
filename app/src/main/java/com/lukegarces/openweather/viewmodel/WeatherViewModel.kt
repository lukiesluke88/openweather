package com.lukegarces.openweather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukegarces.openweather.data.model.ApiResult
import com.lukegarces.openweather.data.model.WeatherListResponse
import com.lukegarces.openweather.data.model.WeatherResponse
import com.lukegarces.openweather.data.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {
    private val _weatherState = MutableStateFlow<ApiResult<WeatherResponse>>(ApiResult.Loading)
    val weatherState: StateFlow<ApiResult<WeatherResponse>> = _weatherState

    private val _weatherListState =
        MutableStateFlow<ApiResult<WeatherListResponse>>(ApiResult.Loading)

    val weatherListState: StateFlow<ApiResult<WeatherListResponse>> = _weatherListState

    fun loadWeather(cityName: String) {
        viewModelScope.launch {
            _weatherState.value = ApiResult.Loading
            _weatherState.value = repository.getWeather(cityName)
        }
    }

    fun loadWeatherList(cityName: String) {
        viewModelScope.launch {
            _weatherListState.value = ApiResult.Loading
            _weatherListState.value = repository.getWeatherList(cityName)
        }
    }
}
