package com.example.capyvocab_fe.user.test.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Displays a single option item for a question
 */
@Composable
fun OptionItem(
    option: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    onClick: () -> Unit,
    isMultipleChoice: Boolean
) {
    val backgroundColor = when {
        isCorrect -> Color(0xFFE6F7E9)
        isSelected -> Color(0xFFE3F2FD)
        else -> Color(0xFFF5F5F5)
    }

    val borderColor = when {
        isCorrect -> Color(0xFF4CAF50)
        isSelected -> Color(0xFF42B3FF)
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Selection indicator - Sử dụng icon phù hợp cho từng loại
        if (isMultipleChoice) {
            // Checkbox cho multiple choice
            Icon(
                imageVector = if (isSelected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF42B3FF) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        } else {
            // Radio button cho single choice
            Icon(
                imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF42B3FF) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Option text
        Text(
            text = option,
            fontSize = 15.sp
        )
    }
}

@Preview
@Composable
private fun OptionItemPreview() {
    OptionItem(
        option = "Option 1",
        isSelected = true,
        isCorrect = false,
        onClick = {},
        isMultipleChoice = true
    )
}