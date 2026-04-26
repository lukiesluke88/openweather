package com.lukegarces.openweather.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
    var showRegister by rememberSaveable { mutableStateOf(false) }

    when (val state = loginState) {

        is LoginState.Success -> {
            WeatherMainScreen(
                user = state.user,
                onLogout = {
                    authViewModel.logout()
                    showRegister = false
                }
            )
        }

        else -> {
            AnimatedContent(
                targetState = showRegister,
                transitionSpec = {
                    if (targetState) {
                        slideInHorizontally(
                            animationSpec = tween(300),
                            initialOffsetX = { fullWidth -> fullWidth }
                        ) togetherWith slideOutHorizontally(
                            animationSpec = tween(300),
                            targetOffsetX = { fullWidth -> -fullWidth }
                        )
                    } else {
                        slideInHorizontally(
                            animationSpec = tween(300),
                            initialOffsetX = { fullWidth -> -fullWidth }
                        ) togetherWith slideOutHorizontally(
                            animationSpec = tween(300),
                            targetOffsetX = { fullWidth -> fullWidth }
                        )
                    }
                },
                label = "AuthScreenAnimation"
            ) { isRegister ->
                if (isRegister) {
                    RegistrationScreen(
                        viewModel = authViewModel,
                        onRegisterSuccess = {
                            showRegister = false
                        },
                        onBackToLogin = {
                            showRegister = false
                        }
                    )
                } else {
                    SignInScreen(
                        viewModel = authViewModel,
                        onLoginSuccess = {},
                        onRegisterClick = {
                            showRegister = true
                        }
                    )
                }
            }
        }
    }
}
