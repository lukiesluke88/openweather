package com.lukegarces.openweather.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lukegarces.openweather.data.remote.RetrofitInstance
import com.lukegarces.openweather.data.repository.WeatherRepository
import com.lukegarces.openweather.viewmodel.WeatherViewModel
import com.lukegarces.openweather.viewmodel.WeatherViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainView() {
    val repository = remember {
        WeatherRepository(RetrofitInstance.api)
    }

    val viewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory(repository)
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )

    val scope = rememberCoroutineScope()

    Column {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = { Text("Current Weather") }
            )

            Tab(
                selected = pagerState.currentPage == 1,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = { Text("List Weather") }
            )
        }

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> WeatherScreen(viewModel)
                1 -> WeatherListScreen(viewModel)
            }
        }
    }
}
