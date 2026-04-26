package com.lukegarces.openweather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukegarces.openweather.data.model.LoginState
import com.lukegarces.openweather.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            try {
                val result = repository.login(email, password)

                result.onSuccess { user ->
                    _loginState.value = LoginState.Success(user)
                }.onFailure { e ->
                    _loginState.value = LoginState.Error(
                        e.message ?: "Login failed"
                    )
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(
                    e.message ?: "Login failed"
                )
            }
        }
    }

    fun logout() {
        _loginState.value = LoginState.Idle
    }
}
