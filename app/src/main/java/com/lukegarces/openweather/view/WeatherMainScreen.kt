package com.lukegarces.openweather.view

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lukegarces.openweather.R
import com.lukegarces.openweather.data.model.LocationHelper
import com.lukegarces.openweather.data.model.User
import com.lukegarces.openweather.data.remote.RetrofitInstance
import com.lukegarces.openweather.data.repository.WeatherRepository
import com.lukegarces.openweather.view.component.ConfirmAlertDialog
import com.lukegarces.openweather.view.component.HeaderNavigationContent
import com.lukegarces.openweather.view.component.WeatherTabsContent
import com.lukegarces.openweather.viewmodel.WeatherViewModel
import com.lukegarces.openweather.viewmodel.WeatherViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherMainScreen(user: User, onLogout: () -> Unit) {
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }
    val repository = remember {
        WeatherRepository(RetrofitInstance.api, locationHelper = LocationHelper(context))
    }

    val viewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory(repository)
    )

    LaunchedEffect(Unit) {
        viewModel.loadWeatherByCurrentLocation()
        viewModel.loadWeatherList("manila")
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ConfirmAlertDialog(
        show = showLogoutDialog,
        title = "Logout",
        message = "Are you sure you want to logout?",
        confirmText = "Yes",
        dismissText = "Cancel",
        onConfirm = {
            showLogoutDialog = false
            onLogout()
        },
        onDismiss = {
            showLogoutDialog = false
        }
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                HeaderNavigationContent(user)

                Divider()

                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            showLogoutDialog = true
                        }
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
