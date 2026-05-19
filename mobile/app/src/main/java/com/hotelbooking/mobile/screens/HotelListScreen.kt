package com.hotelbooking.mobile.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hotelbooking.mobile.domain.model.Hotel
import com.hotelbooking.mobile.viewmodel.HotelViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelListScreen(
    viewModel: HotelViewModel,
    onHotelClick: (UUID) -> Unit,
    onLogout: () -> Unit,
    onNavigateToMyBookings: () -> Unit,
    onBack: () -> Unit,
    isLoggedIn: Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rezultate Căutare") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Înapoi") }
                },
                actions = {
                    if (isLoggedIn) {
                        TextButton(onClick = onNavigateToMyBookings) {
                            Text("Rezervări")
                        }
                        TextButton(onClick = onLogout) {
                            Text("Logout")
                        }
                    } else {
                        TextButton(onClick = onLogout) { // Navigate to login
                            Text("Login")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            viewModel.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (viewModel.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (viewModel.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                        Text("⚠️", style = MaterialTheme.typography.headlineLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Eroare de conexiune la server.",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            viewModel.error ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = onBack) {
                            Text("Încearcă din nou")
                        }
                    }
                }
            } else if (viewModel.hotels.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                        Text("🔍", style = MaterialTheme.typography.headlineLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Ne pare rău, nu am găsit nicio ofertă disponibilă.",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Text(
                            "Încercați o altă locație sau alte date de călătorie.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                LazyColumn {
                    items(viewModel.hotels) { hotel ->
                        HotelItem(hotel = hotel, onClick = { onHotelClick(hotel.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun HotelItem(hotel: Hotel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(hotel.name, style = MaterialTheme.typography.headlineSmall)
            Text(hotel.location, style = MaterialTheme.typography.bodyMedium)
            hotel.description?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, maxLines = 2)
            }
        }
    }
}
