package com.lukegarces.openweather.data.model

data class WeatherResponse(
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val name: String,
)

data class WeatherListResponse(
    val list: List<WeatherItem>
)

data class Coord(
    val lon: Double,
    val lat: Double
)

data class LocationInfo(
    val coord: Coord,
    val city: String,
    val country: String
)

data class User(
    val name: String = "",
    val email: String = "",
    val password: String = ""
)

data class WeatherItem(
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>
)

data class Weather(
    val main: String?,
    val description: String?,
    val icon: String?
)

data class Main(
    val temp: Double
)

data class Sys(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
