package com.example.capyvocab_fe.admin.user.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.user.domain.model.User
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.FocusComponent
import com.example.capyvocab_fe.core.ui.components.FormActionButtons
import com.example.capyvocab_fe.core.ui.components.OverlaySnackbar
import com.example.capyvocab_fe.ui.theme.dimens

@Composable
fun UserFormDialog(
    user: User?,
    errorMessage: String,
    onDismiss: () -> Unit,
    onSave: (User, String?, String?, Uri?) -> Unit,
    onDelete: (() -> Unit)
) {
    var username by remember { mutableStateOf(user?.username ?: "") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var roleId by remember { mutableStateOf(user?.roleId ?: 2) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val dialogWidth = if (screenWidth > 600.dp) 500.dp else screenWidth - 32.dp

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Surface(
                shape = RoundedCornerShape(MaterialTheme.dimens.medium1),
                color = Color(0xFF66E6FF),
                modifier = Modifier
                    .width(dialogWidth)
                    .padding(MaterialTheme.dimens.small1)
            ) {
                Column(
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.medium1)
                        .verticalScroll(rememberScrollState())
                ) {
                    //avatar + id + role
                    FocusComponent {
                        UserFormHeader(
                            userId = user?.id,
                            avatarUrl = selectedImageUri?.toString() ?: user?.avatar,
                            selectedRoleId = roleId,
                            onRoleChange = { roleId = it },
                            onAvatarSelected = { uri -> selectedImageUri = uri }
                        )
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

                    // Text fields
                    FocusComponent {
                        UserFormFields(
                            username = username,
                            onUsernameChange = { username = it },
                            password = password,
                            onPasswordChange = { password = it },
                            confirmPassword = confirmPassword,
                            onConfirmPasswordChange = { confirmPassword = it },
                            email = email,
                            onEmailChange = { email = it }
                        )
                    }

                    if (user != null) {
                        Spacer(Modifier.height(MaterialTheme.dimens.medium2))

                        FocusComponent {
                            user.lastStudyDate?.let {
                                UserInfoStats(
                                    totalStudyDay = user.totalStudyDay,
                                    streak = user.streak,
                                    lastStudyDate = it,
                                    totalLearnedCard = user.totalLearnedCard,
                                    totalMasteredCard = user.totalMasteredCard
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                    // Action buttons
                    FormActionButtons(
                        isEditMode = if (user == null) false else true,
                        onDelete = onDelete,
                        onCancel = onDismiss,
                        onSave = {
                            val updatedUser = user?.copy(
                                username = username,
                                email = email,
                                roleId = roleId,
                            ) ?: User(
                                id = 0,
                                username = username,
                                email = email,
                                avatar = "",
                                status = "NOT_VERIFIED",
                                streak = 0,
                                lastStudyDate = "",
                                totalStudyDay = 0,
                                totalLearnedCard = 0,
                                totalMasteredCard = 0,
                                roleId = roleId,
                            )
                            onSave(updatedUser, password, confirmPassword, selectedImageUri)
                        }
                    )
                }
            }
        }
        OverlaySnackbar(message = errorMessage)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFormHeader(
    userId: Int?,
    avatarUrl: String?,
    selectedRoleId: Int,
    onRoleChange: (Int) -> Unit,
    onAvatarSelected: (Uri) -> Unit
) {
    val roles = listOf(
        1 to "Admin",
        2 to "Miễn phí",
        3 to "Premium"
    )

    var expanded by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { onAvatarSelected(it) } }
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .clickable {
                    imagePickerLauncher.launch("image/*")
                }
        ) {
            if (avatarUrl.isNullOrBlank() || avatarUrl == "N/A") {
                Image(
                    painter = painterResource(R.drawable.add_avt),
                    contentDescription = "Chọn ảnh đại diện",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "Avatar người dùng",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(Modifier.width(10.dp))

        Column {
            if (userId != null) {
                Text("ID: $userId", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Vai trò:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.width(MaterialTheme.dimens.small1))
                Box {
                    // Field để hiển thị
                    BasicTextField(
                        value = roles.find { it.first == selectedRoleId }?.second ?: "",
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Start,
                            lineHeight = 15.sp,
                        ),
                        modifier = Modifier
                            .width(128.dp)
                            .zIndex(0f)
                            .background(Color.Transparent),
                        decorationBox = { innerTextField ->
                            OutlinedTextFieldDefaults.DecorationBox(
                                value = roles.find { it.first == selectedRoleId }?.second ?: "",
                                innerTextField = innerTextField,
                                enabled = true,
                                singleLine = true,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = remember { MutableInteractionSource() },
                                contentPadding = PaddingValues(
                                    start = 12.dp,
                                    top = 10.dp,
                                    bottom = 10.dp,
                                    end = 0.dp
                                ),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                    )
                                },
                                colors = defaultTextFieldColors(),
                                container = {
                                    OutlinedTextFieldDefaults.ContainerBox(
                                        enabled = true,
                                        isError = false,
                                        interactionSource = remember { MutableInteractionSource() },
                                        shape = RoundedCornerShape(MaterialTheme.dimens.small2),
                                        colors = defaultTextFieldColors()
                                    )
                                }
                            )
                        }
                    )

                    // Lớp trong suốt bắt click
                    Spacer(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(MaterialTheme.dimens.small2))
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
        Text("Tên đăng nhập", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            modifier = Modifier.fillMaxWidth(),
            colors = defaultTextFieldColors(),
            singleLine = true,
            shape = RoundedCornerShape(MaterialTheme.dimens.small3),
            textStyle = MaterialTheme.typography.bodyMedium
        )

        Text("Email", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            colors = defaultTextFieldColors(),
            singleLine = true,
            shape = RoundedCornerShape(MaterialTheme.dimens.small3),
            textStyle = MaterialTheme.typography.bodyMedium
        )

        Text("Mật khẩu", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
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
            shape = RoundedCornerShape(MaterialTheme.dimens.small3),
            textStyle = MaterialTheme.typography.bodyMedium
        )

        Text("Xác nhận mật khẩu", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
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
            shape = RoundedCornerShape(MaterialTheme.dimens.small3),
            textStyle = MaterialTheme.typography.bodyMedium
        )
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
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                ) {
                    append("Tổng ngày học: ")
                }
                withStyle(style = SpanStyle(color = Color.DarkGray)) {
                    append("$totalStudyDay")
                }
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                ) {
                    append("Chuỗi: ")
                }
                withStyle(style = SpanStyle(color = Color.DarkGray)) {
                    append("$streak")
                }
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                ) {
                    append("Ngày học cuối: ")
                }
                withStyle(style = SpanStyle(color = Color.DarkGray)) {
                    append(lastStudyDate)
                }
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                ) {
                    append("Tổng thẻ đã học: ")
                }
                withStyle(style = SpanStyle(color = Color.DarkGray)) {
                    append("$totalLearnedCard")
                }
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                ) {
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
        username = "Snoopy",
        email = "duongkhanhngoc@gmail.com",
        avatar = "https://example.com/avatar.png",
        status = "VERIFIED",
        streak = 20,
        lastStudyDate = "10/04/2025",
        totalStudyDay = 20,
        totalLearnedCard = 100,
        totalMasteredCard = 70,
        roleId = 3
    )
        UserFormDialog(
            user = sampleUser,
            errorMessage = "",
            onDismiss = {},
            onSave = { User, password, confirmPassword, selectedAvt ->
            },
            onDelete = {}
        )
}
