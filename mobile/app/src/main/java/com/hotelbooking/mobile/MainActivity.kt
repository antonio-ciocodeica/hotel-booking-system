package com.hotelbooking.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier

import com.hotelbooking.mobile.ui.theme.HotelBookingMobileTheme
import com.hotelbooking.mobile.navigation.MainAuthScreen
import com.hotelbooking.mobile.network.RetrofitClient


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.init(this)
        enableEdgeToEdge()
        setContent {
            HotelBookingMobileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Apelam componenta principala care gestioneaza ecranele
                    MainAuthScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

