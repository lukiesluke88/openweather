package com.lukegarces.openweather.data.model

data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

data class WeatherListResponse(
    val list: List<WeatherItem>
)

data class WeatherItem(
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>
)

data class Coord(
    val lon: Double,
    val lat: Double
)

data class Weather(
    val id: Int?,
    val main: String?,
    val description: String?,
    val icon: String?
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int,
    val grnd_level: Int
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)

data class Clouds(
    val all: Int
)

data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

val mockWeatherResponse = WeatherResponse(
    coord = Coord(
        lon = 120.98,
        lat = 14.60
    ),
    weather = listOf(
        Weather(
            id = 800,
            main = "Clear",
            description = "clear sky",
            icon = "01d"
        )
    ),
    base = "stations",
    main = Main(
        temp = 300.15,        // Kelvin (~27°C)
        feels_like = 303.15,
        temp_min = 298.15,
        temp_max = 302.15,
        pressure = 1012,
        humidity = 70,
        sea_level = 1012,
        grnd_level = 1008
    ),
    visibility = 10000,
    wind = Wind(
        speed = 3.5,
        deg = 120,
        gust = 5.0
    ),
    clouds = Clouds(
        all = 10
    ),
    dt = 1713960000,
    sys = Sys(
        type = 1,
        id = 1234,
        country = "PH",
        sunrise = 1713920000,
        sunset = 1713965000
    ),
    timezone = 28800,
    id = 1701668,
    name = "Manila",
    cod = 200
)