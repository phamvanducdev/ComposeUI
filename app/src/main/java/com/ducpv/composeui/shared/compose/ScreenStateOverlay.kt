package com.ducpv.composeui.shared.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ducpv.composeui.R
import com.ducpv.composeui.shared.component.AppAlertDialog
import com.ducpv.composeui.shared.state.ScreenState
import com.ducpv.composeui.shared.theme.ThemeColor
import com.ducpv.composeui.shared.theme.color

/**
 * Created by pvduc9773 on 11/06/2023.
 */
@Composable
fun ScreenStateOverlay(
    screenState: State<ScreenState>,
    onDismiss: () -> Unit = {},
) {
    when (val state = screenState.value) {
        is ScreenState.Progressing -> {
            ProgressingOverlay()
        }
        is ScreenState.Failed -> {
            if (!state.message.isNullOrEmpty()) {
                AppAlertDialog(
                    title = stringResource(id = R.string.error),
                    message = state.message,
                    confirm = stringResource(id = R.string.ok),
                    cancelable = false,
                    onConfirmClick = onDismiss,
                )
            }
        }
        else -> {
            // Nothing
        }
    }
}

@Composable
fun ProgressingOverlay(
    dismissOnBackPress: Boolean = false,
    dismissOnClickOutside: Boolean = false,
    onDismiss: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismiss,
        DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
        ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                color = ThemeColor.White.color,
            )
        }
    }
}
