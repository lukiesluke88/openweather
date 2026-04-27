package com.lukegarces.openweather.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionManager(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "user_session")

    companion object {
        val KEY_EMAIL = stringPreferencesKey("email")
    }

    suspend fun saveUser(email: String) {
        context.dataStore.edit {
            it[KEY_EMAIL] = email
        }
    }

    suspend fun clearUser() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    val userEmail: Flow<String?> = context.dataStore.data
        .map { it[KEY_EMAIL] }
}