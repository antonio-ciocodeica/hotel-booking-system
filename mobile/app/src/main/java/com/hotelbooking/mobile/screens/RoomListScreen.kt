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
    viewModel: HotelViewModel,
    onRoomClick: (UUID) -> Unit,
    onBack: () -> Unit
) {
    LaunchedEffect(hotelId) {
        viewModel.loadRooms(hotelId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Camere disponibile") },
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
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(viewModel.rooms) { room ->
                    RoomItem(room = room, onClick = { onRoomClick(room.id) })
                }
            }
        }
    }
}

@Composable
fun RoomItem(room: Room, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(room.roomName, style = MaterialTheme.typography.headlineSmall)
            Text("Număr camera: ${room.roomNumber}", style = MaterialTheme.typography.bodyMedium)
            Text("Capacitate: ${room.adultCapacity ?: 0} Adulți, ${room.childCapacity ?: 0} Copii", style = MaterialTheme.typography.bodySmall)
            Text("Preț de bază: ${room.basePrice} EUR", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
            room.roomFacilities?.let {
                Text("Facilități: $it", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                if (room.status == 0) "Disponibilă" else "Indisponibilă",
                color = if (room.status == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}
