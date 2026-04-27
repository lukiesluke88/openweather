package com.lukegarces.openweather.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.lukegarces.openweather.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {
    private val gson = Gson()
    companion object {
        val KEY_NAME = stringPreferencesKey("name")
        val KEY_EMAIL = stringPreferencesKey("email")

        val KEY_WEATHER = stringPreferencesKey("weather_json")
    }

    suspend fun saveUser(name: String, email: String) {
        context.dataStore.edit {
            it[KEY_NAME] = name
            it[KEY_EMAIL] = email
        }
    }

    suspend fun clearUser() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    val user: Flow<Pair<String?, String?>> = context.dataStore.data
        .map {
            val name = it[KEY_NAME]
            val email = it[KEY_EMAIL]
            name to email
        }

    suspend fun saveWeather(weather: WeatherResponse) {
        val json = gson.toJson(weather)

        context.dataStore.edit {
            it[KEY_WEATHER] = json
        }
    }

    val cachedWeather: Flow<WeatherResponse?> = context.dataStore.data
        .map { preferences ->
            preferences[KEY_WEATHER]?.let { json ->
                try {
                    gson.fromJson(json, WeatherResponse::class.java)
                } catch (e: Exception) {
                    null
                }
            }
        }
}
