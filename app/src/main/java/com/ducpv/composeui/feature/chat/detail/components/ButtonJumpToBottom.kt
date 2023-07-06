package com.ducpv.composeui.feature.chat.detail.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Created by pvduc9773 on 19/06/2023.
 */
@Composable
fun ButtonJumpToBottom(
    enabled: Boolean,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Show Jump to Bottom button
    val visibilityTransition = updateTransition(
        targetState = enabled,
        label = "JumpToBottom visibility animation",
    )
    val bottomOffset by visibilityTransition.animateDp(label = "JumpToBottom offset animation") { visibility ->
        if (visibility) {
            32.dp
        } else {
            (-32).dp
        }
    }
    if (bottomOffset > 0.dp) {
        FloatingActionButton(
            onClick = onClicked,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = modifier
                .offset(x = 0.dp, y = -bottomOffset)
                .height(36.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDownward,
                modifier = Modifier.height(18.dp),
                contentDescription = null,
            )
        }
    }
}
