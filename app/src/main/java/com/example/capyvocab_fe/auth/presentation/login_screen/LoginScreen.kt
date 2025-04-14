package com.example.capyvocab_fe.auth.presentation.login_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.LoadingDialog
import com.example.capyvocab_fe.navigation.Route


@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController
   // onLoginSuccess: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Clear form mỗi lần LoginScreen hiển thị
    LaunchedEffect(Unit) {
        viewModel.clearForm()
    }
//    // Navigate when login is successful
//    LaunchedEffect(state.isLoggedIn) {
//        if (state.isLoggedIn) {
//            onLoginSuccess()
//        }
//    }

    LoginContent(
        state = state,
        onUsernameChanged = { viewModel.onUsernameChanged(it) },
        onPasswordChanged = { viewModel.onPasswordChanged(it) },
        onTogglePasswordVisibility = { viewModel.onTogglePasswordVisibility() },
        onLoginClick = { viewModel.login() },
        onRegisterClick = {
            navController.navigate(Route.RegisterScreen.route)
        },
        onGoogleLoginClick = {

        }
    )
}
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val state = LoginViewState(
        isLoading = false,
        username = "",
        password = "",
        isPasswordVisible = false,
        errorMessage = "",
        isLoggedIn = false
    )

    LoginContent(
        state = state,
        onUsernameChanged = { /* Handle username change */ },
        onPasswordChanged = { /* Handle password change */ },
        onTogglePasswordVisibility = {},
        onLoginClick = { /* Handle login click */ },
        onRegisterClick = { },
        onGoogleLoginClick = { /* Handle Google login click */ }
    )
}
@Composable
fun LoginContent(
    state: LoginViewState,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onGoogleLoginClick: () -> Unit
){
    LoadingDialog(isLoading = state.isLoading)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // Username Field
            OutlinedTextField(
                value = state.username,
                onValueChange = onUsernameChanged,
                label = { Text("Tên đăng nhập") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                colors = defaultTextFieldColors(),
                singleLine = true,
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password Field
            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChanged,
                label = { Text("Mật khẩu") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = onTogglePasswordVisibility) {
                        val icon = if (state.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = defaultTextFieldColors(),
                singleLine = true,
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Forgot Password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { /* Handle forgot password */ }) {
                    Text(
                        "Quên mật khẩu?",
                        color = Color.DarkGray,
                        style = TextStyle(textDecoration = TextDecoration.Underline)
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))

            // Login Button
            Button(
                onClick = onLoginClick,
                modifier = Modifier.width(250.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0866FF),
                    contentColor = Color.White
                )
            ) {
                Text("Đăng nhập")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Register Button
            OutlinedButton(
                onClick = onRegisterClick,
                modifier = Modifier.width(250.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Gray
                )
            ) {
                Text("Đăng ký")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sign in with Google
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    color = Color.Black
                )
                Text(
                    text = "Đăng nhập bằng",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.Black
                )
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            IconButton(
                onClick = onGoogleLoginClick,
                modifier = Modifier.size(48.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_ic),
                    contentDescription = "Google Sign In",
                    modifier = Modifier.size(36.dp)
                )
            }

            // Error message
            if (state.errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = state.errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}