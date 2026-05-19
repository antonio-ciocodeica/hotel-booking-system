package com.hotelbooking.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.hotelbooking.mobile.navigation.AppNavigation
import com.hotelbooking.mobile.ui.theme.HotelBookingMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        
        setContent {
            HotelBookingMobileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Use innerPadding to ensure content is not covered by system bars
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}
