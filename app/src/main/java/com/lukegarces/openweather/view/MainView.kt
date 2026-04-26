package com.lukegarces.openweather.view

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lukegarces.openweather.data.model.LoginState
import com.lukegarces.openweather.data.remote.AuthApiService
import com.lukegarces.openweather.data.repository.AuthRepository
import com.lukegarces.openweather.viewmodel.AuthViewModel
import com.lukegarces.openweather.viewmodel.AuthViewModelFactory

@Composable
fun MainView() {
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            AuthRepository(
                AuthApiService()
            )
        )
    )

    val loginState by authViewModel.loginState.collectAsState()

    when (loginState) {
        is LoginState.Success -> {
            WeatherMainScreen(
                onLogout = {
                    authViewModel.logout()
                }
            )
        }

        else -> {
            SignInScreen(
                viewModel = authViewModel,
                onLoginSuccess = {},
                onRegisterClick = {}
            )
        }
    }
}
