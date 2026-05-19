package com.hotelbooking.mobile.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.hotelbooking.mobile.screens.HomeScreen
import com.hotelbooking.mobile.screens.LoginScreen
import com.hotelbooking.mobile.screens.RegisterScreen
import com.hotelbooking.mobile.screens.HotelListScreen


@Composable
fun MainAuthScreen(
    modifier: Modifier = Modifier
) {

    var currentScreen by remember {
        mutableStateOf("login")
    }

    when (currentScreen) {

        "login" -> LoginScreen(
            modifier = modifier,

            onNavigateToRegister = {
                currentScreen = "register"
            },

            onLoginSuccess = {
                currentScreen = "home"
            }
        )

        "register" -> RegisterScreen(
            modifier = modifier,

            onNavigateToLogin = {
                currentScreen = "login"
            }
        )

        "home" -> HomeScreen(
            modifier = modifier,

            onLogout = {
                currentScreen = "login"
            },
            onNavigateToHotels = {
                currentScreen = "hotels"
            }
        )

        "hotels" -> HotelListScreen(
            onBack = {
                currentScreen = "home"
            }
        )
    }
}