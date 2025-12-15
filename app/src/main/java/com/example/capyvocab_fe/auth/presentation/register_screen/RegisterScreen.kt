package com.example.capyvocab_fe.auth.presentation.register_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.FocusComponent
import com.example.capyvocab_fe.core.ui.components.LoadingDialog
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.dimens

@Composable
internal fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    navController: NavController
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigateToOtp.collect {
            navController.navigate(Route.OtpScreen.route) {
                popUpTo(Route.RegisterScreen.route) { inclusive = true }
            }
        }
    }

    FocusComponent {
        RegisterContent(
            state = state,
            onEmailChanged = viewModel::onEmailChanged,
            onUsernameChanged = viewModel::onUsernameChanged,
            onPasswordChanged = viewModel::onPasswordChanged,
            onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
            onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
            onToggleConfirmPasswordVisibility = viewModel::onToggleConfirmPasswordVisibility,
            onRegisterClick = viewModel::register,
            onLoginClick = { navController.popBackStack() }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {

        RegisterContent(
            state = RegisterViewState(
                username = "",
                email = "",
                password = "",
                confirmPassword = "",
                isPasswordVisible = false,
                isConfirmPasswordVisible = false,
                isRegistered = false
            ),
            onEmailChanged = {},
            onUsernameChanged = {},
            onPasswordChanged = {},
            onConfirmPasswordChanged = {},
            onTogglePasswordVisibility = {},
            onToggleConfirmPasswordVisibility = {},
            onRegisterClick = {},
            onLoginClick = {}
        )

}


@Composable
fun RegisterContent(
    state: RegisterViewState,
    onEmailChanged: (String) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    LoadingDialog(isLoading = state.isLoading)
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.medium1) //16dp
        ) {
            Spacer(modifier = Modifier.height(430.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Username
                OutlinedTextField(
                    value = state.username,
                    onValueChange = onUsernameChanged,
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    placeholder = { Text("Tên đăng nhập", style = MaterialTheme.typography.titleMedium) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = defaultTextFieldColors(),
                    singleLine = true,
                    shape = RoundedCornerShape(MaterialTheme.dimens.small3), //15dp
                    textStyle = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

                // Email
                OutlinedTextField(
                    value = state.email,
                    onValueChange = onEmailChanged,
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    placeholder = { Text("Email", style = MaterialTheme.typography.titleMedium) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = defaultTextFieldColors(),
                    singleLine = true,
                    shape = RoundedCornerShape(MaterialTheme.dimens.small3),
                    textStyle = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

                // Password
                OutlinedTextField(
                    value = state.password,
                    onValueChange = onPasswordChanged,
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    placeholder = { Text("Mật khẩu", style = MaterialTheme.typography.titleMedium) },
                    trailingIcon = {
                        IconButton(onClick = onTogglePasswordVisibility) {
                            Icon(
                                imageVector = if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = defaultTextFieldColors(),
                    singleLine = true,
                    shape = RoundedCornerShape(MaterialTheme.dimens.small3),
                    textStyle = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

                // Confirm password
                OutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = onConfirmPasswordChanged,
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    placeholder = { Text("Nhập lại mật khẩu", style = MaterialTheme.typography.titleMedium) },
                    trailingIcon = {
                        IconButton(onClick = onToggleConfirmPasswordVisibility) {
                            Icon(
                                imageVector = if (state.isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (state.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = defaultTextFieldColors(),
                    singleLine = true,
                    shape = RoundedCornerShape(MaterialTheme.dimens.small3),
                    textStyle = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

                // Create account btn
                Button(
                    onClick = onRegisterClick,
                    enabled = state.password.isNotEmpty() && state.password == state.confirmPassword,
                    modifier = Modifier
                        .height(MaterialTheme.dimens.buttonHeight)
                        .width(MaterialTheme.dimens.large * 4),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0866FF))
                ) {
                    Text("Tạo tài khoản", color = Color.White, style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = onLoginClick) {
                        Text(
                            "Đã có tài khoản? Đăng nhập",
                            color = Color.DarkGray,
                            style = MaterialTheme.typography.titleMedium.copy(
                                textDecoration = TextDecoration.Underline,
                                letterSpacing = 0.7.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
                // Error message
                if (state.errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
                    Text(
                        text = state.errorMessage,
                        color = Color.Red,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}