package com.example.capyvocab_fe.auth.presentation.otp_screen

import android.widget.Space
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.ui.theme.Roboto

@Composable
fun OtpScreenContent(
    otp: List<String>,
    onOtpChange: (Int, String) -> Unit,
    onResendClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
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
                .padding(top = 250.dp)
        ) {

            Text("Xác nhận Email của bạn", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Nhập mã OTP tại đây", fontSize = 20.sp, color = Color.DarkGray)

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.Center) {
                repeat(6) { index ->
                    OutlinedTextField(
                        value = otp.getOrNull(index) ?: "",
                        onValueChange = { newChar ->
                            if (newChar.length <= 1 && newChar.all { it.isDigit() }) {
                                onOtpChange(index, newChar)
                            }
                        },
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        ),
                        modifier = Modifier
                            .size(56.dp)
                            .padding(4.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = defaultTextFieldColors()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = onResendClick) {
                Text("Chưa nhận được mã? Gửi lại", color = Color(0xFF0866FF))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onConfirmClick,
                modifier = Modifier
                    .height(44.dp)
                    .width(200.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0866FF))
            ) {
                Text("Xác nhận", color = Color.White)
            }
        }
    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewOTPVerificationScreen() {
    val otpState = remember { mutableStateOf(List(6) { "" }) }

    OtpScreenContent(
        otp = otpState.value,
        onOtpChange = { index, value ->
            otpState.value = otpState.value.toMutableList().also { it[index] = value }
        },
        onResendClick = { /* Xử lý gửi lại mã */ },
        onConfirmClick = { /* Xử lý xác nhận mã */ }
    )
}
