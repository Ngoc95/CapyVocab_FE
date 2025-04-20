package com.example.capyvocab_fe.admin.user.presentation.users_screen.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.capyvocab_fe.admin.user.domain.model.User
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme

@Composable
fun UserFormDialog(
    user: User?,
    onDismiss: () -> Unit,
    onSave: (User, String?, String?) -> Unit,
    onDelete: (() -> Unit)
) {
    var username by remember { mutableStateOf(user?.username ?: "") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var roleId by remember { mutableStateOf(user?.roleId ?: 2) }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val dialogWidth = if (screenWidth > 600.dp) 500.dp else screenWidth - 32.dp

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF66E6FF),
            modifier = Modifier
                .width(dialogWidth)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                //avatar + id + premium
                UserFormHeader(
                    userId = user?.id,
                    avatarUrl = user?.avatar,
                    selectedRoleId = roleId,
                    onRoleChange = { roleId = it }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Text fields
                UserFormFields(
                    username = username,
                    onUsernameChange = { username = it },
                    password = password,
                    onPasswordChange = { password = it },
                    confirmPassword = confirmPassword,
                    onConfirmPasswordChange = {confirmPassword = it},
                    email = email,
                    onEmailChange = { email = it }
                )

                if (user != null) {
                    Spacer(Modifier.height(16.dp))

                    UserInfoStats(
                        totalStudyDay = user.totalStudyDay,
                        streak = user.streak,
                        lastStudyDate = user.lastStudyDate,
                        totalLearnedCard = user.totalLearnedCard,
                        totalMasteredCard = user.totalMasteredCard
                    )
                }

                Spacer(Modifier.height(10.dp))

                // Action buttons
                UserFormActions(
                    isEditMode = if(user == null) false else true,
                    onDelete = onDelete,
                    onCancel = onDismiss,
                    onSave = {
                        val updatedUser = user?.copy(
                            username = username,
//                            password = password,
                            email = email,
                            roleId = roleId
                        ) ?: User(
                            id = 0,
                            username = username,
//                            password = password,
                            email = email,
                            avatar = "",
                            roleId = roleId,
                            streak = 0,
                            lastStudyDate = "",
                            totalStudyDay = 0,
                            totalLearnedCard = 0,
                            totalMasteredCard = 0,
                            status = "NOT_VERIFIED"
                        )
                        onSave(updatedUser, password, confirmPassword)
                    }
                )
            }
        }
    }
}

@Composable
fun UserFormHeader(
    userId: Int?,
    avatarUrl: String?,
    selectedRoleId: Int,
    onRoleChange: (Int) -> Unit
) {
    val roles = listOf(
        1 to "Admin",
        2 to "Miễn phí",
        3 to "Premium"
    )

    var expanded by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(8.dp))

        Column {
            if (userId != null) {
                Text("ID: $userId", fontWeight = FontWeight.Bold)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Vai trò:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Box {
                    // Field để hiển thị
                    OutlinedTextField(
                        value = roles.find { it.first == selectedRoleId }?.second ?: "",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .width(160.dp)
                            .zIndex(0f), // đảm bảo nằm dưới Spacer clickable
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                        shape = RoundedCornerShape(15.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                        ),
                    )

                    // Lớp trong suốt bắt click
                    Spacer(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(15.dp))
                            .zIndex(1f)
                            .clickable { expanded = true }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        roles.forEach { (id, name) ->
                            DropdownMenuItem(
                                onClick = {
                                    onRoleChange(id)
                                    expanded = false
                                },
                                text = { Text(name) }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun UserFormFields(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Tên đăng nhập", fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            modifier = Modifier.fillMaxWidth(),
            colors = defaultTextFieldColors(),
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )

        Text("Email", fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            colors = defaultTextFieldColors(),
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )

        Text("Mật khẩu", fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = defaultTextFieldColors(),
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )

        Text("Xác nhận mật khẩu", fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = defaultTextFieldColors(),
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )
    }
}


@Composable
fun UserFormActions(
    isEditMode: Boolean,
    onDelete: (() -> Unit),
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isEditMode) {
            TextButton(onClick = onDelete) {
                Text("Xoá", color = Color(0xFF240000))
            }
        } else {
            Spacer(modifier = Modifier.width(8.dp)) // giữ layout cân bằng khi không có nút xoá
        }
        Row {
            TextButton(onClick = onCancel) {
                Text("Huỷ", color = Color(0xFF240000))
            }
            TextButton(onClick = onSave) {
                Text("Lưu", color = Color(0xFF240000))
            }
        }
    }
}

@Composable
fun UserInfoStats(
    totalStudyDay: Int,
    streak: Int,
    lastStudyDate: String,
    totalLearnedCard: Int,
    totalMasteredCard: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            buildAnnotatedString{
                withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold, color = Color.Black)) {
                    append("Tổng ngày học: ")
                }
                withStyle(style = SpanStyle(color = Color.DarkGray)) {
                    append("$totalStudyDay")
                }
            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            buildAnnotatedString{
                withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold, color = Color.Black)) {
                    append("Chuỗi: ")
                }
                withStyle(style = SpanStyle(color = Color.DarkGray)) {
                    append("$streak")
                }
            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            buildAnnotatedString{
                withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold, color = Color.Black)) {
                    append("Ngày học cuối: ")
                }
                withStyle(style = SpanStyle(color = Color.DarkGray)) {
                    append(lastStudyDate)
                }
            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            buildAnnotatedString{
                withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold, color = Color.Black)) {
                    append("Tổng thẻ đã học: ")
                }
                withStyle(style = SpanStyle(color = Color.DarkGray)) {
                    append("$totalLearnedCard")
                }
            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            buildAnnotatedString{
                withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold, color = Color.Black)) {
                    append("Tổng thẻ thành thạo: ")
                }
                withStyle(style = SpanStyle(color = Color.DarkGray)) {
                    append("$totalMasteredCard")
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun UserFormModalPreview() {
    val sampleUser = User(
        id = 1,
        email = "duongkhanhngoc@gmail.com",
        username = "Snoopy",
        avatar = "https://example.com/avatar.png",
        status = "VERIFIED",
        streak = 20,
        lastStudyDate = "10/04/2025",
        totalStudyDay = 20,
        totalLearnedCard = 100,
        totalMasteredCard = 70,
        roleId = 1,
    )
    CapyVocab_FETheme {
        UserFormDialog(
            user = sampleUser,
            onDismiss = {},
            onSave = { User, password, confirmPassword ->
            },
            onDelete = {}
        )
    }
}
