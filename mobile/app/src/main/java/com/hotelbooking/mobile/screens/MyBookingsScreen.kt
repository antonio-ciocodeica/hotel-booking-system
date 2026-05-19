package com.hotelbooking.mobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hotelbooking.mobile.domain.model.Booking
import com.hotelbooking.mobile.viewmodel.BookingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen(
    viewModel: BookingViewModel,
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadMyBookings()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rezervările Mele") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Înapoi") }
                }
            )
        }
    ) { padding ->
        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (viewModel.myBookings.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Nu aveți nicio rezervare momentan.")
                }
            } else {
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(viewModel.myBookings) { booking ->
                        BookingItem(
                            booking = booking,
                            onCancel = { viewModel.cancelBooking(booking.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookingItem(booking: Booking, onCancel: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Rezervare ID: ${booking.id.toString().take(8)}...", style = MaterialTheme.typography.titleMedium)
            Text("Check-In: ${booking.checkIn}", style = MaterialTheme.typography.bodyMedium)
            Text("Check-Out: ${booking.checkOut}", style = MaterialTheme.typography.bodyMedium)
            Text("Preț: ${booking.price} EUR", style = MaterialTheme.typography.bodyMedium)
            
            val statusText = when(booking.status) {
                0 -> "PENDING"
                1 -> "CONFIRMED"
                2 -> "COMPLETED"
                3 -> "CANCELED"
                else -> "UNKNOWN"
            }
            Text("Status: $statusText", color = if (booking.status == 3) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)

            if (booking.status != 3) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onCancel, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text("Anulează Rezervarea")
                }
            }
        }
    }
}
