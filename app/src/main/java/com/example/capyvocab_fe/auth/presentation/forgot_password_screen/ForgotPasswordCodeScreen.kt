package com.example.capyvocab_fe.auth.presentation.forgot_password_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme


@Composable
fun ForgotPasswordCodeScreen(
    email: String,
    viewModel: ForgotPasswordCodeViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Khi thành công -> Quay về màn đăng nhập
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.navigate(Route.LoginScreen.route) {
                popUpTo(0)
            }
        }
    }

    ForgotPasswordCodeContent(
        state = state,
        onCodeChanged = viewModel::onCodeChanged,
        onNewPasswordChanged = viewModel::onNewPasswordChanged,
        onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
        onResetPasswordClick = { viewModel.resetPassword(email) },
        onResendCodeClick = { viewModel.resendCode(email) }
    )
}

@Composable
fun ForgotPasswordCodeContent(
    state: ForgotPasswordCodeState,
    onCodeChanged: (String) -> Unit,
    onNewPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onResetPasswordClick: () -> Unit,
    onResendCodeClick: () -> Unit
) {
    var isNewPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

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
            Text(
                "Nhập mã xác thực & mật khẩu mới",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))

            // Mã xác thực
            OutlinedTextField(
                value = state.code,
                onValueChange = onCodeChanged,
                placeholder = { Text("Mã xác thực") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))

            // Mật khẩu mới
            OutlinedTextField(
                value = state.newPassword,
                onValueChange = onNewPasswordChanged,
                placeholder = { Text("Mật khẩu mới") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isNewPasswordVisible = !isNewPasswordVisible }) {
                        Icon(
                            imageVector = if (isNewPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isNewPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                        )
                    }
                }
            )
            Spacer(Modifier.height(12.dp))

            // Xác nhận mật khẩu mới
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = onConfirmPasswordChanged,
                placeholder = { Text("Xác nhận mật khẩu mới") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        Icon(
                            imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isConfirmPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                        )
                    }
                }
            )
            Spacer(Modifier.height(16.dp))

            Button(onClick = onResetPasswordClick) {
                Text("Đổi mật khẩu")
            }

            Spacer(Modifier.height(12.dp))

            // Hiển thị đếm ngược
            if (!state.canResend) {
                val minutes = state.remainingTime / 60
                val seconds = (state.remainingTime % 60).toString().padStart(2, '0')
                Text(
                    "Mã xác thực còn hiệu lực trong: $minutes:$seconds",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Button(onClick = onResendCodeClick) {
                    Text("Gửi lại mã xác thực")
                }
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

@Preview(showBackground = true)
@Composable
fun ForgotPasswordCodeScreenPreview() {
    CapyVocab_FETheme {
        val sampleState = ForgotPasswordCodeState(
            code = "123456",
            newPassword = "MySecret1",
            confirmPassword = "MySecret1",
            isLoading = false,
            errorMessage = "Mật khẩu mới không phù hợp",
            isSuccess = false
        )
        ForgotPasswordCodeContent(
            state = sampleState,
            onCodeChanged = {},
            onNewPasswordChanged = {},
            onConfirmPasswordChanged = {},
            onResetPasswordClick= {},
            onResendCodeClick = {}
        )
    }
}
