package com.lukegarces.openweather.data.repository

import com.lukegarces.openweather.data.model.User
import com.lukegarces.openweather.data.remote.AuthApiService

class AuthRepository(
    private val apiService: AuthApiService
) {
    suspend fun login(email: String, password: String): Result<User> {
        return apiService.loginUser(email, password)
    }

    suspend fun register(user: User): Result<Unit> {
        return apiService.registerUser(user)
    }
}
