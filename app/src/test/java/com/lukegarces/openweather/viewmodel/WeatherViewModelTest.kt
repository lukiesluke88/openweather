package com.lukegarces.openweather.viewmodel

import com.lukegarces.openweather.data.model.ApiResult
import com.lukegarces.openweather.data.model.Coord
import com.lukegarces.openweather.data.model.Main
import com.lukegarces.openweather.data.model.Sys
import com.lukegarces.openweather.data.model.Weather
import com.lukegarces.openweather.data.model.WeatherItem
import com.lukegarces.openweather.data.model.WeatherListResponse
import com.lukegarces.openweather.data.model.WeatherResponse
import com.lukegarces.openweather.data.repository.WeatherRepository
import com.lukegarces.openweather.util.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: WeatherRepository
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setup() {
        repository = mock()

        viewModel = WeatherViewModel(
            repository = repository,
            dispatcher = mainDispatcherRule.testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadWeatherReturnsSuccessState() = runTest {

        val fakeWeather = WeatherResponse(
            coord = Coord(
                lon = 120.98,
                lat = 14.60
            ),
            weather = listOf(
                Weather(
                    main = "Clouds",
                    description = "broken clouds",
                    icon = "04d"
                )
            ),
            base = "stations",
            main = Main(temp = 30.5),
            dt = 1714118400,
            sys = Sys(
                country = "PH",
                sunrise = 1714082400,
                sunset = 1714126800
            ),
            timezone = 28800,
            name = "Manila"
        )

        whenever(repository.getWeather("Manila"))
            .thenReturn(ApiResult.Success(fakeWeather))

        viewModel.loadWeather("Manila")

        advanceUntilIdle()

        val state = viewModel.weatherState.value

        assertTrue(state is ApiResult.Success)

        val result = state as ApiResult.Success

        assertEquals("Manila", result.data.name)
        assertEquals("PH", result.data.sys.country)
        assertEquals(1714126800, result.data.sys.sunset)
        assertEquals(1714082400, result.data.sys.sunrise)
        assertEquals(30.5, result.data.main.temp, 0.0)

        verify(repository).getWeather("Manila")
    }

    @Test
    fun loadWeatherListReturnsSuccess() = runTest {
        val fakeList = WeatherListResponse(
            list = listOf(
                WeatherItem(
                    dt_txt = "1714082400",
                    main = Main(temp = 31.2),
                    weather = listOf(
                        Weather(
                            main = "Clear",
                            description = "clear sky",
                            icon = "01d"
                        )
                    )
                )
            )
        )

        whenever(repository.getWeatherList("Manila"))
            .thenReturn(ApiResult.Success(fakeList))

        viewModel.loadWeatherList("Manila")

        advanceUntilIdle()

        val state = viewModel.weatherListState.value

        assertTrue(state is ApiResult.Success)

        val result = state as ApiResult.Success

        assertEquals(1, result.data.list.size)
        assertEquals("1714082400", result.data.list[0].dt_txt)
        assertEquals(31.2, result.data.list[0].main.temp, 0.01)
        assertEquals("Clear", result.data.list[0].weather[0].main)

        verify(repository).getWeatherList("Manila")
    }

    @Test
    fun loadWeatherByCurrentLocation() = runTest {
        val fakeWeather = WeatherResponse(
            coord = Coord(
                lon = 120.98,
                lat = 14.60
            ),
            weather = listOf(
                Weather(
                    main = "Clouds",
                    description = "broken clouds",
                    icon = "04d"
                )
            ),
            base = "stations",
            main = Main(temp = 30.5),
            dt = 1714118400,
            sys = Sys(
                country = "PH",
                sunrise = 1714082400,
                sunset = 1714126800
            ),
            timezone = 28800,
            name = "Manila"
        )

        whenever(repository.getWeatherByCurrentLocation())
            .thenReturn(ApiResult.Success(fakeWeather))

        viewModel.loadWeatherByCurrentLocation()

        advanceUntilIdle()

        val state = viewModel.weatherState.value

        assertTrue(state is ApiResult.Success)

        val success = state as ApiResult.Success

        assertEquals("Manila", success.data.name)
        assertEquals("PH", success.data.sys.country)
        assertEquals(30.5, success.data.main.temp, 0.0)

        verify(repository).getWeatherByCurrentLocation()
    }
}
