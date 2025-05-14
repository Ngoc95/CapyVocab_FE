package com.example.capyvocab_fe.admin.course.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.course.domain.model.CourseLevel
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.FormActionButtons
import com.example.capyvocab_fe.core.ui.components.OverlaySnackbar

@Composable
fun CourseFormDialog(
    course: Course?,
    errorMessage: String,
    onDismiss: () -> Unit,
    onSave: (Course) -> Unit,
    onDelete: () -> Unit
) {
    var title by remember { mutableStateOf(course?.title ?: "") }
    var level by remember { mutableStateOf(course?.level ?: CourseLevel.BEGINNER.value) }
    var target by remember { mutableStateOf(course?.target ?: "") }
    var description by remember { mutableStateOf(course?.description ?: "") }

    var expanded by remember { mutableStateOf(false) }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val dialogWidth = if (screenWidth > 600.dp) 500.dp else screenWidth - 32.dp

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 16.dp)
        ) {
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
                    // Header (Course ID)
                    course?.id?.let {
                        Text("ID: $it", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Form fields
                    Text("Tên khoá học", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        maxLines = 5,
                        colors = defaultTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Mục tiêu", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = target,
                        onValueChange = { target = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        maxLines = 5,
                        colors = defaultTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Trình độ", fontWeight = FontWeight.Bold)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = level,
                            onValueChange = { /*read-only */ },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp),
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                    contentDescription = "Dropdown Arrow",
                                    modifier = Modifier.clickable { expanded = !expanded }
                                )
                            },
                            colors = defaultTextFieldColors()
                        )

                        Spacer(
                            modifier = Modifier
                                .matchParentSize()
                                .clip(RoundedCornerShape(13.dp))
                                .zIndex(1f)
                                .clickable { expanded = true }
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            CourseLevel.entries.forEach { courseLevel ->
                                DropdownMenuItem(
                                    text = { Text(courseLevel.value) },
                                    onClick = {
                                        level = courseLevel.value
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Mô tả", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        maxLines = 5,
                        colors = defaultTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action buttons
                    FormActionButtons(
                        isEditMode = course != null,
                        onDelete = onDelete,
                        onCancel = onDismiss,
                        onSave = {
                            val updatedCourse = course?.copy(
                                title = title,
                                level = level,
                                target = target,
                                description = description
                            ) ?: Course(
                                id = 0,
                                title = title,
                                level = level,
                                target = target,
                                description = description,
                                courseTopics = emptyList()
                            )
                            onSave(updatedCourse)
                        }
                    )
                }
            }
        }
        OverlaySnackbar(message = errorMessage)
    }
}

