package com.hotelbooking.mobile.navigation

import androidx.compose.runtime.*
import com.hotelbooking.mobile.di.ServiceLocator
import com.hotelbooking.mobile.screens.*
import com.hotelbooking.mobile.viewmodel.*
import java.util.UUID

sealed class Screen {
    object Login : Screen()
    object Register : Screen()
    object Search : Screen()
    object HotelList : Screen()
    object MyBookings : Screen()
    data class RoomTypeList(val hotelId: UUID) : Screen()
    data class RoomList(val hotelId: UUID, val roomTypeId: UUID) : Screen()
    data class Booking(val roomId: UUID, val roomTypeId: UUID) : Screen()
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { 
        mutableStateOf<Screen>(
            if (ServiceLocator.authRepository.isLoggedIn()) Screen.Search else Screen.Login
        )
    }

    // Preserve ViewModels across navigation to share data (like search dates)
    val loginViewModel = remember { ServiceLocator.provideLoginViewModel() }
    val registerViewModel = remember { ServiceLocator.provideRegisterViewModel() }
    val hotelViewModel = remember { ServiceLocator.provideHotelViewModel() }
    val bookingViewModel = remember { ServiceLocator.provideBookingViewModel() }

    when (val screen = currentScreen) {
        is Screen.Login -> LoginScreen(
            viewModel = loginViewModel,
            onNavigateToRegister = { currentScreen = Screen.Register },
            onLoginSuccess = { currentScreen = Screen.Search }
        )
        is Screen.Register -> RegisterScreen(
            viewModel = registerViewModel,
            onNavigateToLogin = { currentScreen = Screen.Login }
        )
        is Screen.Search -> SearchScreen(
            onSearch = { location, checkIn, checkOut ->
                hotelViewModel.searchHotels(location, checkIn, checkOut) {
                    currentScreen = Screen.HotelList
                }
            },
            onNavigateToMyBookings = { currentScreen = Screen.MyBookings },
            onLogout = {
                ServiceLocator.authRepository.logout()
                currentScreen = Screen.Login
            },
            isLoggedIn = ServiceLocator.authRepository.isLoggedIn()
        )
        is Screen.HotelList -> HotelListScreen(
            viewModel = hotelViewModel,
            onHotelClick = { hotelId -> currentScreen = Screen.RoomTypeList(hotelId) },
            onLogout = {
                ServiceLocator.authRepository.logout()
                currentScreen = Screen.Login
            },
            onNavigateToMyBookings = { currentScreen = Screen.MyBookings },
            onBack = { currentScreen = Screen.Search },
            isLoggedIn = ServiceLocator.authRepository.isLoggedIn()
        )
        is Screen.MyBookings -> MyBookingsScreen(
            viewModel = bookingViewModel,
            onBack = { 
                hotelViewModel.refreshSearch()
                currentScreen = Screen.Search 
            }
        )
        is Screen.RoomTypeList -> RoomTypeListScreen(
            hotelId = screen.hotelId,
            viewModel = hotelViewModel,
            onRoomTypeClick = { roomTypeId -> currentScreen = Screen.RoomList(screen.hotelId, roomTypeId) },
            onBack = { currentScreen = Screen.HotelList }
        )
        is Screen.RoomList -> RoomListScreen(
            hotelId = screen.hotelId,
            roomTypeId = screen.roomTypeId,
            viewModel = hotelViewModel,
            onRoomClick = { roomId ->
                if (ServiceLocator.authRepository.isLoggedIn()) {
                    bookingViewModel.resetBookingMessage()
                    currentScreen = Screen.Booking(roomId, screen.roomTypeId)
                } else {
                    currentScreen = Screen.Login
                }
            },
            onBack = { currentScreen = Screen.RoomTypeList(screen.hotelId) }
        )
        is Screen.Booking -> BookingScreen(
            roomId = screen.roomId,
            roomType = hotelViewModel.roomTypes.find { it.id == screen.roomTypeId },
            initialCheckIn = hotelViewModel.checkInDate,
            initialCheckOut = hotelViewModel.checkOutDate,
            viewModel = bookingViewModel,
            onBookingSuccess = { 
                hotelViewModel.clearRooms()
                currentScreen = Screen.MyBookings 
            },
            onBack = { currentScreen = Screen.HotelList }
        )
    }
}
