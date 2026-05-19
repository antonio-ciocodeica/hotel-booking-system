package com.hotelbooking.mobile.di

import com.hotelbooking.mobile.data.api.AuthApi
import com.hotelbooking.mobile.data.api.BookingApi
import com.hotelbooking.mobile.data.api.HotelApi
import com.hotelbooking.mobile.data.repository.AuthRepositoryImpl
import com.hotelbooking.mobile.data.repository.BookingRepositoryImpl
import com.hotelbooking.mobile.data.repository.HotelRepositoryImpl
import com.hotelbooking.mobile.domain.repository.AuthRepository
import com.hotelbooking.mobile.domain.repository.BookingRepository
import com.hotelbooking.mobile.domain.repository.HotelRepository
import com.hotelbooking.mobile.network.RetrofitClient
import com.hotelbooking.mobile.viewmodel.BookingViewModel
import com.hotelbooking.mobile.viewmodel.HotelViewModel
import com.hotelbooking.mobile.viewmodel.LoginViewModel
import com.hotelbooking.mobile.viewmodel.RegisterViewModel

object ServiceLocator {

    private val authApi: AuthApi by lazy { RetrofitClient.api }
    private val bookingApi: BookingApi by lazy { RetrofitClient.bookingApi }
    private val hotelApi: HotelApi by lazy { RetrofitClient.hotelApi }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authApi, RetrofitClient.getTokenManager())
    }

    val hotelRepository: HotelRepository by lazy {
        HotelRepositoryImpl(hotelApi)
    }

    val bookingRepository: BookingRepository by lazy {
        BookingRepositoryImpl(bookingApi)
    }

    fun provideLoginViewModel() = LoginViewModel(authRepository)
    fun provideRegisterViewModel() = RegisterViewModel(authRepository)
    fun provideHotelViewModel() = HotelViewModel(hotelRepository)
    fun provideBookingViewModel() = BookingViewModel(bookingRepository)
}
