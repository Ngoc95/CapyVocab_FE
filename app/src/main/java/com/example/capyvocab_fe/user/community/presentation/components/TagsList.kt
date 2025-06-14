package com.example.capyvocab_fe.user.community.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.ui.theme.MyLightBlue
import com.example.capyvocab_fe.ui.theme.White

@Composable
fun TagsList(
    tags: List<String>?,
    onDeleteTag:(String) -> Unit,
    onAddTag: (String) -> Unit,
) {
    var inputTag by rememberSaveable { mutableStateOf("") }
    val tagsPerRow = 2
    val rows = if (tags != null)  tags.chunked(tagsPerRow) else null

    Column {
        TextField(
            value = inputTag,
            onValueChange = {newTag -> inputTag = newTag},
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF2F2F2),
                focusedContainerColor = Color(0xFF91DFFF),
                cursorColor = Color(0xFF5E4A45),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    text = "Nhập tag...",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    val tag = inputTag.trim()
                    if (tag.isNotEmpty()) {
                        onAddTag(tag)
                        inputTag = ""
                    }
                }
            )
        )

        HorizontalDivider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )

        if(rows != null)
        {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(0.dp,10.dp,10.dp,10.dp)
            ) {
                rows.forEach { rowTags ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowTags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .shadow(6.dp, RoundedCornerShape(16.dp))
                                    .background(MyLightBlue, RoundedCornerShape(16.dp))
                                    .padding(10.dp, 5.dp, 10.dp, 5.dp)
                            ) {
                                Text(
                                    text = tag,
                                    color = White,
                                    modifier = Modifier
                                        .padding(end = 40.dp)
                                        .align(Alignment.CenterStart)
                                )

                                IconButton(
                                    onClick = {
                                        onDeleteTag(tag)
                                    },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.CenterEnd)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Xóa",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun TagPreview() {

    val tagList = listOf("tAGnnnnn1", "ielts")

    TagsList(
        tags = tagList,
        onAddTag = {},
        onDeleteTag = {},
    )
}

