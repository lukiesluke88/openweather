package com.lukegarces.openweather.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lukegarces.openweather.view.WeatherListScreen
import com.lukegarces.openweather.view.WeatherScreen
import com.lukegarces.openweather.viewmodel.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WeatherTabsContent(
    padding: PaddingValues, pagerState: PagerState,
    scope: CoroutineScope, viewModel: WeatherViewModel
) {

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

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
