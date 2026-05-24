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
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Rezervare ID: ${booking.id.toString().take(8)}...", style = MaterialTheme.typography.titleMedium)
            Text("Check-In: ${booking.checkIn}", style = MaterialTheme.typography.bodyMedium)
            Text("Check-Out: ${booking.checkOut}", style = MaterialTheme.typography.bodyMedium)
            Text("Preț Total: ${booking.price} EUR", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
            
            // Sync status with backend: 0=Pending, 1=Validated (Check-in), 3=Checked-out, 4=Canceled
            val statusText = when(booking.status) {
                0 -> "ÎN AȘTEPTARE"
                1 -> "CONFIRMATĂ (ACTIVE)"
                3 -> "FINALIZATĂ (CHECK-OUT)"
                4 -> "ANULATĂ"
                else -> "STATUS: ${booking.status}"
            }
            
            val statusColor = when(booking.status) {
                4 -> MaterialTheme.colorScheme.error
                3 -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.primary
            }
            
            Text("Status: $statusText", color = statusColor, style = MaterialTheme.typography.bodyMedium)

            if (booking.status == 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onCancel, 
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Anulează Rezervarea")
                }
            }
        }
    }
}
