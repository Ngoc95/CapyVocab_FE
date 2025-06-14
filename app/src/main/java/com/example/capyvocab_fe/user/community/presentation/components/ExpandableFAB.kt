package com.example.capyvocab_fe.user.community.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.capyvocab_fe.ui.theme.MyLightBlue

@Composable
fun ExpandableFAB(
    onCreatePost: () -> Unit,
    onMyPosts: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp),
            horizontalAlignment = Alignment.End
        ) {
            // FAB phụ 1
            AnimatedVisibility(visible = expanded) {
                FloatingActionButton(
                    shape = CircleShape,
                    onClick = {
                        onCreatePost()
                    },
                    containerColor = MyLightBlue,
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Create Post")
                }
            }

            // FAB phụ 2
            AnimatedVisibility(visible = expanded) {
                FloatingActionButton(
                    shape = CircleShape,
                    onClick = { onMyPosts() },
                    containerColor = MyLightBlue,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Restore, contentDescription = "My Posts")
                }
            }

            // FAB chính
            FloatingActionButton(
                shape = CircleShape,
                onClick = { expanded = !expanded },
                containerColor = MyLightBlue,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.Close else Icons.Default.MoreVert,
                    contentDescription = "Mở rộng"
                )
            }
        }
    }
}
