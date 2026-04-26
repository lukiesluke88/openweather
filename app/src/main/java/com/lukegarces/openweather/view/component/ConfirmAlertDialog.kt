package com.lukegarces.openweather.view.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun ConfirmAlertDialog(
    show: Boolean,
    title: String,
    message: String,
    confirmText: String = "Yes",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!show) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        }
    )
}

@Preview(showBackground = true, widthDp = 300, heightDp = 640)
@Composable
fun ConfirmAlertDialogPreview() {
    MaterialTheme {
        ConfirmAlertDialog(
            show = true,
            title = "Logout",
            message = "Are you sure you want to logout?",
            onConfirm = {},
            onDismiss = {}
        )
    }
}