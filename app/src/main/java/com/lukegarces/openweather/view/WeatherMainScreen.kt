package com.lukegarces.openweather.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lukegarces.openweather.R
import com.lukegarces.openweather.data.model.User
import com.lukegarces.openweather.data.remote.RetrofitInstance
import com.lukegarces.openweather.data.repository.WeatherRepository
import com.lukegarces.openweather.view.component.WeatherTabsContent
import com.lukegarces.openweather.viewmodel.WeatherViewModel
import com.lukegarces.openweather.viewmodel.WeatherViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherMainScreen(user: User, onLogout: () -> Unit) {

    val repository = remember {
        WeatherRepository(RetrofitInstance.api)
    }

    val viewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory(repository)
    )

    LaunchedEffect(Unit) {
        viewModel.loadWeatherList("manila")
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 50.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Divider()

                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    }
                )
            }
        }) {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch { drawerState.open() }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }) { padding ->
            WeatherTabsContent(
                padding = padding,
                pagerState = pagerState,
                scope = scope,
                viewModel = viewModel
            )
        }
    }
}
