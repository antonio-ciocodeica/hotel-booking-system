package com.hotelbooking.mobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hotelbooking.mobile.model.Hotel
import com.hotelbooking.mobile.network.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun HotelListScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val api = RetrofitClient.hotelApi

    var hotels by remember { mutableStateOf<List<Hotel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            isLoading = true
            try {
                val response = api.getHotels()

                if (response.isSuccessful) {
                    hotels = response.body() ?: emptyList()
                } else {
                    error = "Eroare la încărcare hoteluri"
                }

            } catch (e: Exception) {
                error = e.message ?: "Eroare rețea"
            }

            isLoading = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "🏨 Hoteluri disponibile",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        }

        if (error.isNotEmpty()) {
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }

        LazyColumn {
            items(hotels) { hotel ->
                HotelCard(hotel)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Înapoi")
        }
    }
}