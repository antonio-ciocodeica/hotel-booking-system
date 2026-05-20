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
import coil.compose.AsyncImage
import com.hotelbooking.mobile.domain.model.RoomType
import com.hotelbooking.mobile.viewmodel.HotelViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomTypeListScreen(
    hotelId: UUID,
    viewModel: HotelViewModel,
    onRoomTypeClick: (UUID) -> Unit,
    onBack: () -> Unit
) {
    LaunchedEffect(hotelId) {
        viewModel.loadRoomTypes(hotelId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tipuri de Camere") },
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
            } else if (viewModel.roomTypes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Nu sunt tipuri de camere disponibile.")
                }
            } else {
                LazyColumn {
                    items(viewModel.roomTypes) { roomType ->
                        RoomTypeItem(roomType = roomType, onClick = { onRoomTypeClick(roomType.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun RoomTypeItem(roomType: RoomType, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            if (roomType.imageUrls.isNotEmpty()) {
                AsyncImage(
                    model = roomType.imageUrls.first(),
                    contentDescription = "Imagine ${roomType.roomName}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(roomType.roomName, style = MaterialTheme.typography.headlineSmall)
                roomType.roomFacilities?.let {
                    Text(it, style = MaterialTheme.typography.bodyMedium)
                }
                Text("Capacitate: ${roomType.adultCapacity ?: 0} adulți, ${roomType.childCapacity ?: 0} copii", style = MaterialTheme.typography.bodySmall)
                Text("Preț de bază: ${roomType.basePrice} EUR", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                
                Spacer(modifier = Modifier.height(8.dp))
                Text("Vezi camere disponibile", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
