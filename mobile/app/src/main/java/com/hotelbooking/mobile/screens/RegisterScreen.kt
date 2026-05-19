package com.hotelbooking.mobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.hotelbooking.mobile.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Înregistrare", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = viewModel.firstName,
            onValueChange = { viewModel.firstName = it },
            label = { Text("Prenume") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.lastName,
            onValueChange = { viewModel.lastName = it },
            label = { Text("Nume") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Parolă") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.phoneNumber,
            onValueChange = { viewModel.phoneNumber = it },
            label = { Text("Număr de telefon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.register(onNavigateToLogin) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Înregistrare")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = viewModel.message, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToLogin, enabled = !viewModel.isLoading) {
            Text("Ai deja cont? Autentifică-te aici")
        }
    }
}
