package com.hotelbooking.mobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.hotelbooking.mobile.viewmodel.BookingViewModel
import java.time.LocalDate
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    roomId: UUID,
    roomType: com.hotelbooking.mobile.domain.model.RoomType?,
    initialCheckIn: LocalDate,
    initialCheckOut: LocalDate,
    viewModel: BookingViewModel,
    onBookingSuccess: () -> Unit,
    onBack: () -> Unit
) {
    var checkIn by remember { mutableStateOf(initialCheckIn.toString()) }
    var checkOut by remember { mutableStateOf(initialCheckOut.toString()) }

    val nights = java.time.temporal.ChronoUnit.DAYS.between(initialCheckIn, initialCheckOut)
    val totalPrice = (roomType?.basePrice ?: 0.0) * nights

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Efectuare Rezervare") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Înapoi") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Detalii Rezervare", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            Text("Tip Cameră: ${roomType?.roomName ?: "Cameră"}", style = MaterialTheme.typography.titleMedium)
            Text("Perioada selectată:", style = MaterialTheme.typography.titleMedium)
            Text("$checkIn -> $checkOut ($nights nopți)", style = MaterialTheme.typography.bodyLarge)
            
            Spacer(modifier = Modifier.height(8.dp))
            Text("Preț total estimat: $totalPrice EUR", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    try {
                        viewModel.makeBooking(
                            roomId,
                            LocalDate.parse(checkIn),
                            LocalDate.parse(checkOut),
                            onBookingSuccess
                        )
                    } catch (e: Exception) {
                        viewModel.bookingMessage = "Format dată invalid!"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Rezervă Acum")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(viewModel.bookingMessage, color = MaterialTheme.colorScheme.primary)
        }
    }
}
