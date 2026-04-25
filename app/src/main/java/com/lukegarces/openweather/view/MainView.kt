package com.lukegarces.openweather.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun MainView() {
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }

    if (!isLoggedIn) {
        SignInScreen(
            onSignInClick = { email, password ->
                isLoggedIn = true
            },
            onRegisterClick = {
            }
        )
    } else {
        WeatherMainScreen(onLogout = { isLoggedIn = false })
    }
}
