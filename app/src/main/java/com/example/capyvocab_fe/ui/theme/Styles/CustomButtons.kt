package com.example.capyvocab_fe.ui.theme.Styles

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MyLikeButton(
    voted: Boolean,
    onLikedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        checked = voted,
        onCheckedChange = { post ->
            onLikedChange()
        },
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
    ) {
        Icon(
            imageVector = if (voted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = if (voted) "Liked" else "Like",
            tint = if (voted) Color.Red else Color.Gray
        )
    }
}