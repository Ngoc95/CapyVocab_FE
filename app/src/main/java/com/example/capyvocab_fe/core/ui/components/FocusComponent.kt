<<<<<<<< HEAD:app/src/main/java/com/example/capyvocab_fe/core/util/components/FocusComponent.kt
package com.example.capyvocab_fe.core.util.components
========
package com.example.capyvocab_fe.core.ui.components
>>>>>>>> admin_user:app/src/main/java/com/example/capyvocab_fe/core/ui/components/FocusComponent.kt

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun FocusComponent(content: @Composable () -> Unit) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
    ) {
        content()
    }
}