package com.lukegarces.openweather.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukegarces.openweather.data.model.LoginState
import com.lukegarces.openweather.data.model.RegisterState
import com.lukegarces.openweather.data.model.User
import com.lukegarces.openweather.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

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

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading

            val user = User(
                name = name,
                email = email,
                password = password
            )

            Log.d("tag", "called repository.register")
            val result = repository.register(user)

            result.onSuccess {
                _registerState.value = RegisterState.Success
                Log.d("tag", "Success")
            }.onFailure { e ->
                _registerState.value = RegisterState.Error(e.message ?: "Registration failed")
                Log.d("tag", "Error {${e.message}}")
            }
        }
    }

    fun resetRegisterState() {
        _registerState.value = RegisterState.Idle
    }
}
