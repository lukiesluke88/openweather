package com.lukegarces.openweather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lukegarces.openweather.data.SessionManager
import com.lukegarces.openweather.data.repository.WeatherRepository

class WeatherViewModelFactory(
    private val repository: WeatherRepository,
    private val sessionManager: SessionManager
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository, sessionManager = sessionManager) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}