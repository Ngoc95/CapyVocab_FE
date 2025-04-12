package com.example.capyvocab_fe.auth.presentation.login_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    CapyVocab_FETheme {
        RegisterScreen()
    }
}
@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val confirmPasswordVisible = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 15.dp)
        ) {
            // Username
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                placeholder = { Text("Tên đăng nhập") },
                modifier = Modifier.width(330.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color(0xFF0866FF),
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedPlaceholderColor = Color.Gray
                ),
                singleLine = true,
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Email
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                placeholder = { Text("Email") },
                modifier = Modifier.width(330.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color(0xFF0866FF),
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedPlaceholderColor = Color.Gray
                ),
                singleLine = true,
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Password
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                placeholder = { Text("Mật khẩu") },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(
                            imageVector = if (passwordVisible.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.width(330.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color(0xFF0866FF),
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedPlaceholderColor = Color.Gray
                ),
                singleLine = true,
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Confirm password
            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                placeholder = { Text("Nhập lại mật khẩu") },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible.value = !confirmPasswordVisible.value }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.width(330.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color(0xFF0866FF),
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedPlaceholderColor = Color.Gray
                ),
                singleLine = true,
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Create account btn
            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .height(44.dp)
                    .width(250.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0866FF))
            ) {
                Text("Tạo tài khoản", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Login btn
            OutlinedButton(
                onClick = onLoginClick,
                modifier = Modifier
                    .height(44.dp)
                    .width(250.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Gray
                )
            ) {
                Text("Đăng nhập")
            }
        }
    }
}