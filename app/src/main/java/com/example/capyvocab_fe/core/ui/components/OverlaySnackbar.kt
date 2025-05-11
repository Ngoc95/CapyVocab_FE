package com.example.capyvocab_fe.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun OverlaySnackbar(
    message: String,
    modifier: Modifier = Modifier,
) {
    if (message.isNotEmpty()) {
        // Dùng Popup thay vì Dialog để không block interaction
        Popup(
            alignment = Alignment.BottomCenter,
            properties = PopupProperties(focusable = false) // Không block tương tác
        ) {
            Snackbar(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(10.dp),
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ) {
                Text(text = message, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}