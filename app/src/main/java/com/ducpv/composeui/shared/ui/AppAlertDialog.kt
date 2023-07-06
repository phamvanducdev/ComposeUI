package com.ducpv.composeui.shared.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.window.DialogProperties

/**
 * Created by pvduc9773 on 11/06/2023.
 */
@Composable
fun AppAlertDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null,
    confirm: String? = null,
    dismiss: String? = null,
    cancelable: Boolean = true,
    titleFontSize: TextUnit = TextUnit.Unspecified,
    confirmButtonShape: Shape = ButtonDefaults.textShape,
    confirmButtonBorder: BorderStroke? = null,
    dismissButtonColors: ButtonColors = ButtonDefaults.textButtonColors(),
    confirmButtonTextColor: Color = Color.Unspecified,
    dismissButtonTextColor: Color = Color.Unspecified,
    onConfirmClick: () -> Unit = {},
    onDismissClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = cancelable,
            dismissOnClickOutside = cancelable,
        ),
        title = {
            if (!title.isNullOrEmpty()) {
                Text(text = title, fontSize = titleFontSize)
            }
        },
        text = {
            if (!message.isNullOrEmpty()) {
                Text(text = message)
            }
        },
        confirmButton = {
            if (!confirm.isNullOrEmpty()) {
                TextButton(
                    shape = confirmButtonShape,
                    border = confirmButtonBorder,
                    onClick = {
                        onConfirmClick.invoke()
                    },
                ) {
                    Text(text = confirm, color = confirmButtonTextColor)
                }
            }
        },
        dismissButton = {
            if (!dismiss.isNullOrEmpty()) {
                TextButton(
                    colors = dismissButtonColors,
                    onClick = {
                        onDismissClick.invoke()
                    },
                ) {
                    Text(text = dismiss, color = dismissButtonTextColor)
                }
            }
        },
    )
}
