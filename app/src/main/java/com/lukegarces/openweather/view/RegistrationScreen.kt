package com.lukegarces.openweather.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.lukegarces.openweather.data.model.RegisterState
import com.lukegarces.openweather.view.component.customTextFieldColors
import com.lukegarces.openweather.viewmodel.AuthViewModel

@Composable
fun RegistrationScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val registerState by viewModel.registerState.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Register",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                localError = null
            },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = customTextFieldColors(),
            enabled = registerState !is RegisterState.Loading
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                localError = null
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = customTextFieldColors(),
            enabled = registerState !is RegisterState.Loading
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                localError = null
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = customTextFieldColors(),
            enabled = registerState !is RegisterState.Loading
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (localError != null) {
            Text(
                text = localError!!,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        when (registerState) {
            is RegisterState.Error -> {
                Text(
                    text = (registerState as RegisterState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            RegisterState.Success -> {
                AlertDialog(
                    onDismissRequest = {
                        onRegisterSuccess()
                        viewModel.resetRegisterState()
                    },
                    title = { Text("Success") },
                    text = { Text("Registration completed successfully!") },
                    confirmButton = {
                        TextButton(onClick = {
                            onRegisterSuccess()
                            viewModel.resetRegisterState()
                        }) {
                            Text("OK")
                        }
                    }
                )
            }

            else -> Unit
        }

        Button(
            onClick = {
                val nameInput = name.trim()
                val emailInput = email.trim()
                val passwordInput = password.trim()

                if (nameInput.length < 2) {
                    localError = "Name is required"
                    return@Button
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    localError = "Invalid email"
                    return@Button
                }

                if (passwordInput.length < 4) {
                    localError = "Password must be at least 4 characters"
                    return@Button
                }

                Log.d("tag", "Button clicked register")
                viewModel.register(
                    name = nameInput,
                    email = emailInput,
                    password = passwordInput
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = registerState !is RegisterState.Loading
        ) {
            Text(
                text = if (registerState is RegisterState.Loading) "Registering..." else "Submit"
            )
        }

        Button(
            onClick = {
                onBackToLogin()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = registerState !is RegisterState.Loading
        ) {
            Text("Cancel")
        }
    }
}
