package com.example.capyvocab_fe.auth.presentation.forgot_password_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.capyvocab_fe.navigation.Route

@Composable
fun ForgotPasswordEmailScreen(
    viewModel: ForgotPasswordEmailViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Khi trạng thái `isEmailSent` = true -> Điều hướng sang màn nhập mã
    LaunchedEffect(state.isEmailSent) {
        if (state.isEmailSent) {
            navController.navigate(Route.ForgotPasswordCodeScreen.route + "?email=${state.email}")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp)
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Quên mật khẩu", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(12.dp))

            Text("Nhập email của tài khoản", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                placeholder = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(onClick = { viewModel.sendEmail() }) {
                Text("Gửi mã xác thực")
            }

            if (state.isLoading) {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            if (state.errorMessage.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                Text(
                    state.errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
