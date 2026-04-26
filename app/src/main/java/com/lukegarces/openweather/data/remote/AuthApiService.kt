package com.lukegarces.openweather.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.lukegarces.openweather.data.model.User
import kotlinx.coroutines.tasks.await

class AuthApiService {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun loginUser(
        email: String,
        password: String
    ): Result<User> {

        val snapshot = firestore
            .collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()

        val document = snapshot.documents.firstOrNull()

        if (document == null) {
            return Result.failure(Exception("Email not found"))
        }

        val user = document.toObject(User::class.java)

        if (user?.password != password) {
            return Result.failure(Exception("Invalid password"))
        }

        return Result.success(user)
    }
}
