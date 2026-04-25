package com.lukegarces.openweather.data.repository

import com.lukegarces.openweather.BuildConfig
import com.lukegarces.openweather.data.model.ApiResult
import com.lukegarces.openweather.data.model.WeatherListResponse
import com.lukegarces.openweather.data.model.WeatherResponse
import com.lukegarces.openweather.data.remote.ApiService
import retrofit2.HttpException
import java.io.IOException

class WeatherRepository(private val apiService: ApiService) {
    val  keyApi = BuildConfig.WEATHER_API_KEY

    suspend fun getWeather(cityName: String): ApiResult<WeatherResponse> {
        return try {
            val response = apiService.getWeather(
                cityName = cityName,
                apiKey = keyApi
            )
            ApiResult.Success(response)
        } catch (e: IOException) {
            // No internet / network error
            ApiResult.Error("No internet connection")
        } catch (e: HttpException) {
            // API error (404, 401, etc.)
            ApiResult.Error("Server error: ${e.code()}")
        } catch (e: Exception) {
            // Unknown error
            ApiResult.Error(e.message ?: "Something went wrong")
        }
    }

    suspend fun getWeatherList(cityName: String): ApiResult<WeatherListResponse> {
        return try {
            val response = apiService.getWeatherList(
                cityName = cityName,
                apiKey = keyApi
            )
            ApiResult.Success(response)

        } catch (e: IOException) {
            // No internet / network error
            ApiResult.Error("No internet connection")
        } catch (e: HttpException) {
            // API error (404, 401, etc.)
            ApiResult.Error("Server error: ${e.code()}")
        } catch (e: Exception) {
            // Unknown error
            ApiResult.Error(e.message ?: "Something went wrong")
        }
    }
}
