package com.example.capyvocab_fe.auth.presentation.otp_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.core.ui.components.OverlaySnackbar
import com.example.capyvocab_fe.core.ui.components.SnackbarType
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(
    viewModel: OtpViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var remainingSeconds by remember { mutableIntStateOf(60) }
    var canResend by remember { mutableStateOf(false) }
    var visibleError by remember { mutableStateOf("") }
    var visibleSuccess by remember { mutableStateOf("") }

    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage.isNotEmpty()) {
            visibleError = state.errorMessage
            delay(2000)
            visibleError = ""
        }
    }

    LaunchedEffect(state.successMessage) {
        if (state.successMessage.isNotEmpty()) {
            visibleSuccess = state.successMessage
            delay(2000)
            visibleSuccess = ""
        }
    }

    // Countdown timer for resend OTP
    LaunchedEffect(Unit) {
        while (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
        }
        canResend = true
    }
    OtpScreenContent(
        otp = state.otp,
        onOtpChange = viewModel::onOtpChanged,
        onResendClick = {
            viewModel::reSendOtp
            remainingSeconds = 60
            canResend = false
        },
        onVerifyClick = viewModel::verifyOtp,
        error = visibleError,
        loading = state.isLoading,
        remainingSeconds = remainingSeconds,
        canResend = canResend,
        successMessage = visibleSuccess
    )
}

@Composable
fun OtpScreenContent(
    modifier: Modifier = Modifier,
    otp: String,
    onOtpChange: (String) -> Unit,
    onResendClick: () -> Unit,
    onVerifyClick: () -> Unit,
    error: String,
    loading: Boolean,
    remainingSeconds: Int,
    canResend: Boolean,
    successMessage: String
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 300.dp)

        ) {
            Text(
                text = "Xác nhận Email của bạn",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 26.sp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Nhập mã OTP tại đây",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // OTP Input Fields
            OtpInputFields(otp = otp, onOtpChange = {})

            Spacer(modifier = Modifier.height(16.dp))

            // Resend OTP
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Chưa nhận được mã?",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                TextButton(
                    onClick = onResendClick,
                    enabled = canResend && !loading
                ) {
                    Text(
                        text = if (canResend) "Gửi lại" else "Gửi lại (${remainingSeconds}s)",
                        color = if (canResend) Color(0xFF2196F3) else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Verify button
            Button(
                onClick = onVerifyClick,
                enabled = otp.length == 6 && !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0866FF),
                    contentColor = Color.White
                )
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Xác nhận",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        OverlaySnackbar(message = error)
        OverlaySnackbar(message = successMessage, type = SnackbarType.Success)
    }
}

@Composable
fun OtpInputFields(
    otp: String,
    onOtpChange: (String) -> Unit
) {
    val focusRequesters = remember { List(6) { FocusRequester() } }
    val focusManager = LocalFocusManager.current

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(6) { index ->
            val char = otp.getOrNull(index)?.toString() ?: ""

            OutlinedTextField(
                value = char,
                onValueChange = { value ->
                    if (value.isEmpty() || value.all { it.isDigit() }) {
                        val newOtp = otp.toMutableList()
                        if (index < otp.length) {
                            if (value.isEmpty()) {
                                // Xóa ký tự
                                newOtp.removeAt(index)
                                onOtpChange(newOtp.joinToString(""))
                                if (index > 0) {
                                    focusRequesters[index - 1].requestFocus()
                                }
                            } else {
                                // Thay thế ký tự
                                newOtp[index] = value.last()
                                onOtpChange(newOtp.joinToString(""))
                                if (index < 5) {
                                    focusRequesters[index + 1].requestFocus()
                                } else {
                                    focusManager.clearFocus()
                                }
                            }
                        } else if (value.isNotEmpty()) {
                            // Thêm ký tự mới
                            onOtpChange(otp + value.last())
                            if (index < 5) {
                                focusRequesters[index + 1].requestFocus()
                            } else {
                                focusManager.clearFocus()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .focusRequester(focusRequesters[index]),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2C89FF),
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color(0xFF2C89FF),
                    unfocusedTextColor = Color.LightGray
                ),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }

    // Tự động focus vào ô đầu tiên khi màn hình được hiển thị
    LaunchedEffect(Unit) {
        focusRequesters.firstOrNull()?.requestFocus()
    }
}

@Preview
@Composable
private fun OtpScreenPreview() {
    CapyVocab_FETheme {
        OtpScreenContent(
            otp = "",
            onOtpChange = {},
            onResendClick = {},
            onVerifyClick = {},
            error = "",
            loading = false,
            remainingSeconds = 60,
            canResend = true,
            successMessage = ""
        )
    }
}