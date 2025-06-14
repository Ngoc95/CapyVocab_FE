package com.example.capyvocab_fe.admin.topic.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.FormActionButtons
import com.example.capyvocab_fe.core.ui.components.OverlaySnackbar
import com.example.capyvocab_fe.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicFormDialog(
    topic: Topic?,
    errorMessage: String,
    onDismiss: () -> Unit,
    onSave: (Topic, Uri?) -> Unit,
    onDelete: () -> Unit
) {
    var title by remember { mutableStateOf(topic?.title ?: "") }
    var description by remember { mutableStateOf(topic?.description ?: "") }
    var thumbnail by remember { mutableStateOf(topic?.thumbnail ?: "") }
    var type by remember { mutableStateOf(topic?.type ?: "") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var expanded by remember { mutableStateOf(false) }

    // List of available topic types
    val topicTypes = listOf("Free", "Premium")

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                selectedImageUri = it
            }
        }
    )

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val dialogWidth = if (screenWidth > 600.dp) 500.dp else screenWidth - 32.dp

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = MaterialTheme.dimens.small3)
        ) {
            Surface(
                shape = RoundedCornerShape(MaterialTheme.dimens.small3),
                color = Color(0xFF66E6FF),
                modifier = Modifier
                    .width(dialogWidth)
                    .padding(MaterialTheme.dimens.small3)
            ) {
                Column(
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.small3)
                        .verticalScroll(rememberScrollState())
                ) {
                    // header thumbnail, id, type
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(MaterialTheme.dimens.large)
                                .clip(CircleShape)
                                .clickable {
                                    imagePickerLauncher.launch("image/*")
                                }
                        ) {
                            if (selectedImageUri != null) {
                                // Show the selected image immediately
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Ảnh chủ đề đã chọn",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else if (thumbnail.isBlank() || thumbnail == "N/A") {
                                Image(
                                    painter = painterResource(R.drawable.add_avt),
                                    contentDescription = "Chọn ảnh chủ đề",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                AsyncImage(
                                    model = thumbnail,
                                    contentDescription = "Ảnh chủ đề",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(MaterialTheme.dimens.small2))
                        Column {
                            if (topic != null) {
                                Text("ID: ${topic.id}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Loại:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Spacer(modifier = Modifier.width(MaterialTheme.dimens.small2))
                                Box {
                                    BasicTextField(
                                        value = topicTypes.find { it == type } ?: "",
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
                                                value = topicTypes.find { it == type } ?: "",
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
                                        topicTypes.forEach { topicType ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    type = topicType
                                                    expanded = false
                                                },
                                                text = { Text(topicType) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
                    // Form fields
                    Text("Tên chủ đề", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(MaterialTheme.dimens.small3),
                        maxLines = 5,
                        colors = defaultTextFieldColors(),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
                    Text("Mô tả", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(MaterialTheme.dimens.small3),
                        colors = defaultTextFieldColors(),
                        maxLines = Int.MAX_VALUE,
                        minLines = 3,
                        textStyle = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
                    //Action button
                    FormActionButtons(
                        isEditMode = topic != null,
                        onDelete = onDelete,
                        onCancel = onDismiss,
                        onSave = {
                            val updatedTopic = topic?.copy(
                                title = title,
                                description = description,
                                type = type,
                                words = emptyList()
                            ) ?: Topic(
                                id = 0,
                                title = title,
                                description = description,
                                thumbnail = "N/A",
                                type = type,
                                alreadyLearned = false
                            )
                            onSave(updatedTopic, selectedImageUri)
                        }
                    )
                }
            }
        }
        OverlaySnackbar(message = errorMessage)
    }

}

@Preview
@Composable
private fun TopicFormDialogPreview() {
    val sample = Topic(
        id = 1,
        title = "xxxxx",
        description = "xxxxxxxxxxxxxxx",
        thumbnail = "xxxxxxxxxxxxx",
        type = "Premium",
        alreadyLearned = false
    )
        TopicFormDialog(
            topic = sample,
            errorMessage = "",
            onDismiss = {},
            onSave = {w,i ->},
            onDelete = {}
        )
}
