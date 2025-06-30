//package com.example.capyvocab_fe.user.profile.presentation
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.CheckCircle
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//
//import com.example.capyvocab_fe.R
//import com.example.capyvocab_fe.profile.presentation.ProfileViewModel
//import com.example.capyvocab_fe.user.profile.domain.model.ProfileUser
//
//@Composable
//fun ChangePasswordScreen(
//    user: ProfileUser,
//    onBackClick:() -> Unit,
//    viewModel: ProfileViewModel = hiltViewModel(),
//    navController: NavController
//) {
//
//    ChangePasswordScreenContent(
//        onBackClick = onBackClick,
//        onChangePassword = {_,_, -> }
//    )
//
//}
//
//@Composable
//fun ChangePasswordScreenContent(
//    onBackClick: () -> Unit,
//    onChangePassword: (oldPassword: String, newPassword: String) -> Unit
//) {
//    var oldPassword by remember { mutableStateOf("") }
//    var newPassword by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//    var successMessage by remember { mutableStateOf<String?>(null) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)) {
//        // Back button
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(bottom = 20.dp)
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.backicon),
//                modifier = Modifier
//                    .size(40.dp)
//                    .clickable { onBackClick() },
//                contentDescription = null,
//            )
//            Text(
//                text = "Đổi mật khẩu",
//                fontWeight = FontWeight.Bold,
//                fontSize = 20.sp
//            )
//        }
//
//        // Old password
//        Text("Mật khẩu cũ", fontWeight = FontWeight.Bold)
//        TextField(
//            value = oldPassword,
//            onValueChange = { oldPassword = it },
//            singleLine = true,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 16.dp),
//            visualTransformation = PasswordVisualTransformation(),
//            colors = TextFieldDefaults.colors(
//                unfocusedContainerColor = Color.White,
//                focusedContainerColor = Color.White
//            )
//        )
//
//        // New password
//        Text("Mật khẩu mới", fontWeight = FontWeight.Bold)
//        TextField(
//            value = newPassword,
//            onValueChange = { newPassword = it },
//            singleLine = true,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 16.dp),
//            visualTransformation = PasswordVisualTransformation(),
//            colors = TextFieldDefaults.colors(
//                unfocusedContainerColor = Color.White,
//                focusedContainerColor = Color.White
//            )
//        )
//
//        // Confirm password
//        Text("Xác nhận mật khẩu mới", fontWeight = FontWeight.Bold)
//        TextField(
//            value = confirmPassword,
//            onValueChange = { confirmPassword = it },
//            singleLine = true,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 24.dp),
//            visualTransformation = PasswordVisualTransformation(),
//            colors = TextFieldDefaults.colors(
//                unfocusedContainerColor = Color.White,
//                focusedContainerColor = Color.White
//            )
//        )
//
//        // Button
//        Button(
//            onClick = {
//                when {
//                    oldPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank() -> {
//                        errorMessage = "Vui lòng điền đầy đủ thông tin"
//                    }
//                    newPassword != confirmPassword -> {
//                        errorMessage = "Mật khẩu mới không khớp"
//                    }
//                    else -> {
//                        errorMessage = null
//                        onChangePassword(oldPassword, newPassword)
//                        successMessage = "Đổi mật khẩu thành công"
//                    }
//                }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
//        ) {
//            Text("Đổi mật khẩu", color = Color.White)
//        }
//
//        // Error or success message
//        errorMessage?.let {
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(it, color = Color.Red)
//        }
//
//        successMessage?.let {
//            Spacer(modifier = Modifier.height(16.dp))
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .background(Color(0xFF4CAF50), shape = RoundedCornerShape(8.dp))
//                    .padding(horizontal = 16.dp, vertical = 12.dp)
//            ) {
//                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(it, color = Color.White)
//            }
//        }
//    }
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun ChangePasswordPreview() {
//    ChangePasswordScreenContent(
//        onBackClick = { },
//        onChangePassword = { _, _ -> }
//    )
//}
