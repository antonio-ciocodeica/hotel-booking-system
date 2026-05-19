package com.hotelbooking.mobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onSearch: (String, LocalDate, LocalDate) -> Unit,
    onNavigateToMyBookings: () -> Unit,
    onLogout: () -> Unit,
    isLoggedIn: Boolean
) {
    var location by remember { mutableStateOf("") }
    var checkIn by remember { mutableStateOf(LocalDate.now().toString()) }
    var checkOut by remember { mutableStateOf(LocalDate.now().plusDays(1).toString()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Caută Hotel") },
                actions = {
                    if (isLoggedIn) {
                        TextButton(onClick = onNavigateToMyBookings) { Text("Rezervări") }
                        TextButton(onClick = onLogout) { Text("Logout") }
                    } else {
                        TextButton(onClick = onLogout) { Text("Login") }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Unde doriți să mergeți?", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = checkIn,
                onValueChange = { checkIn = it },
                label = { Text("Check-In (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = checkOut,
                onValueChange = { checkOut = it },
                label = { Text("Check-Out (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Oraș / Locație") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage != null) {
                Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = {
                    try {
                        val d1 = LocalDate.parse(checkIn)
                        val d2 = LocalDate.parse(checkOut)
                        if (d2.isBefore(d1) || d2.isEqual(d1)) {
                            errorMessage = "Data de Check-Out trebuie să fie după Check-In."
                        } else {
                            errorMessage = null
                            onSearch(location, d1, d2)
                        }
                    } catch (e: Exception) {
                        errorMessage = "Format dată invalid (YYYY-MM-DD)."
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Caută Oferte", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}
