package com.hotelbooking.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.hotelbooking.mobile.ui.theme.HotelBookingMobileTheme
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// 1. Modelele de date (Exact ca in JSON-ul din Postman)
data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val phoneNumber: String
)

// 2. Interfata Retrofit
interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<Unit>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>
}

// 3. Clientul de retea
object RetrofitClient {
    val api: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // Adresa catre backend-ul local
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }
}

// 4. Activitatea Principala
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

// 5. Gestioneaza trecerea intre Login si Register
@Composable
fun MainAuthScreen(modifier: Modifier = Modifier) {
    // Daca este true aratam Login, daca e false aratam Register
    var isLoginScreen by remember { mutableStateOf(true) }

    if (isLoginScreen) {
        LoginScreen(
            modifier = modifier,
            onNavigateToRegister = { isLoginScreen = false } // Schimba pe Register
        )
    } else {
        RegisterScreen(
            modifier = modifier,
            onNavigateToLogin = { isLoginScreen = true } // Schimba inapoi pe Login
        )
    }
}

// 6. Ecranul de LOGIN
@Composable
fun LoginScreen(modifier: Modifier = Modifier, onNavigateToRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var responseMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Autentificare", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Parolă") },
            visualTransformation = PasswordVisualTransformation(), // Ascunde textul parolei
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    responseMessage = "Se procesează..."
                    try {
                        val response = RetrofitClient.api.login(LoginRequest(email, password))
                        if (response.isSuccessful) {
                            responseMessage = "✅ Login reușit!"
                        } else {
                            responseMessage = "❌ Eroare: Credențiale incorecte"
                        }
                    } catch (e: Exception) {
                        responseMessage = "⚠️ Eroare rețea: ${e.localizedMessage}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = responseMessage, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("Nu ai cont? Înregistrează-te aici")
        }
    }
}

// 7. Ecranul de REGISTER
@Composable
fun RegisterScreen(modifier: Modifier = Modifier, onNavigateToLogin: () -> Unit) {
    // Variabile pentru toate campurile tale din JSON
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var responseMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    // ScrollState in caz ca ecranul e mic si tastatura acopera butoanele
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Înregistrare Cont Nou", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nume (Name)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Prenume (Surname)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Parolă") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Număr Telefon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    responseMessage = "Se trimit datele..."
                    try {
                        val request = RegisterRequest(name, surname, email, password, phoneNumber)
                        val response = RetrofitClient.api.register(request)
                        if (response.isSuccessful) {
                            responseMessage = "✅ Cont creat cu succes!"
                        } else {
                            responseMessage = "❌ Eroare Inregistrare (Ex: Email deja folosit)"
                        }
                    } catch (e: Exception) {
                        responseMessage = "⚠️ Eroare rețea: ${e.localizedMessage}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = responseMessage, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("Ai deja cont? Loghează-te aici")
        }
    }
}