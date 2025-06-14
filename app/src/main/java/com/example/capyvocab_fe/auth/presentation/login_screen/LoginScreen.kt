package com.example.capyvocab_fe.auth.presentation.login_screen

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.capyvocab_fe.ui.theme.dimens
import com.example.capyvocab_fe.util.Constant.GOOGLE_CLIENT_ID
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController
   // onLoginSuccess: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val googleSignInLauncher =
        rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    account?.let { acc ->
                        acc.getIdToken()?.let { token ->
                            viewModel.googleLogin(token)
                        } ?: run {
                            // Xử lý khi không lấy được token
                            Log.e("GoogleSignIn", "Failed to get ID token")
                        }
                    }
                } catch (e: ApiException) {
                    Log.e("GoogleSignIn", "Google sign-in failed", e)
                }
            }
        }

    // Điều hướng khi đăng nhập thành công theo role
    LaunchedEffect(Unit) {
        viewModel.navigateToAdmin.collect {
            navController.navigate(Route.AdminNavigation.route) {
                popUpTo(0) // Xoá toàn bộ backstack nếu cần
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigateToUser.collect {
            navController.navigate(Route.UserNavigation.route) {
                popUpTo(0)
            }
        }
    }
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
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(GOOGLE_CLIENT_ID)
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            googleSignInLauncher.launch(googleSignInClient.signInIntent)
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
                .verticalScroll(rememberScrollState())
                .padding(MaterialTheme.dimens.medium1), //16dp
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2)) //30dp

            // Username Field
            OutlinedTextField(
                value = state.username,
                onValueChange = onUsernameChanged,
                label = { Text("Tên đăng nhập", style = MaterialTheme.typography.titleMedium) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                colors = defaultTextFieldColors(),
                singleLine = true,
                shape = RoundedCornerShape(MaterialTheme.dimens.small3), //15dp
                textStyle = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1)) //8dp

            // Password Field
            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChanged,
                label = { Text("Mật khẩu", style = MaterialTheme.typography.titleMedium) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = onTogglePasswordVisibility) {
                        val icon = if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = defaultTextFieldColors(),
                singleLine = true,
                shape = RoundedCornerShape(MaterialTheme.dimens.small3),
                textStyle = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))

            // Forgot Password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { /* Handle forgot password */ }) {
                    Text(
                        "Quên mật khẩu?",
                        color = Color.DarkGray,
                        style = MaterialTheme.typography.titleMedium.copy(
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))

            // Login Button
            Button(
                onClick = onLoginClick,
                modifier = Modifier.width(MaterialTheme.dimens.large * 4), //250dp
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0866FF),
                    contentColor = Color.White
                )
            ) {
                Text("Đăng nhập", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))

            // Register Button
            OutlinedButton(
                onClick = onRegisterClick,
                modifier = Modifier.width(MaterialTheme.dimens.large * 4),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Gray
                )
            ) {
                Text("Đăng ký", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))

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
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small2),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.Black
                )
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

            IconButton(
                onClick = onGoogleLoginClick,
                modifier = Modifier.size(MaterialTheme.dimens.buttonHeight * 1.2f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_ic),
                    contentDescription = "Google Sign In",
                    modifier = Modifier.size(MaterialTheme.dimens.buttonHeight * 0.9f)
                )
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