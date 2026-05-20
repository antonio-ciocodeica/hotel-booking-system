package com.hotelbooking.mobile.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hotelbooking.mobile.domain.model.Room
import com.hotelbooking.mobile.viewmodel.HotelViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomListScreen(
    hotelId: UUID,
    roomTypeId: UUID,
    viewModel: HotelViewModel,
    onRoomClick: (UUID) -> Unit,
    onBack: () -> Unit
) {
    val roomType = viewModel.roomTypes.find { it.id == roomTypeId }

    LaunchedEffect(roomTypeId) {
        viewModel.loadRooms(roomTypeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Camere: ${roomType?.roomName ?: ""}") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Înapoi") }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            viewModel.error?.let {
                Text(
                    text = "Eroare: $it",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (viewModel.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (viewModel.rooms.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Nu sunt camere disponibile pentru acest tip.")
                }
            } else {
                LazyColumn {
                    items(viewModel.rooms) { room ->
                        RoomItem(
                            room = room,
                            roomTypeName = roomType?.roomName ?: "Cameră",
                            price = roomType?.basePrice ?: 0.0,
                            onClick = { onRoomClick(room.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RoomItem(room: Room, roomTypeName: String, price: Double, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Camera ${room.roomNumber}", style = MaterialTheme.typography.headlineSmall)
            Text("Tip: $roomTypeName", style = MaterialTheme.typography.bodyMedium)
            Text("Status: ${if (room.status == 0) "Disponibilă" else "Ocupată"}", 
                color = if (room.status == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
            Text("Preț: $price EUR", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
            
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onClick, 
                modifier = Modifier.fillMaxWidth(),
                enabled = room.status == 0
            ) {
                Text(if (room.status == 0) "Rezervă" else "Indisponibilă")
            }
        }
    }
}
