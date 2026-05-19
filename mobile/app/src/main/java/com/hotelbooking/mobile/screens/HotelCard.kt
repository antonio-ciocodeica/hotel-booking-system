package com.hotelbooking.mobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hotelbooking.mobile.model.Hotel

@Composable
fun HotelCard(hotel: Hotel) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = hotel.name,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "📍 ${hotel.city}")

            Text(text = "⭐ ${hotel.rating}")
        }
    }
}