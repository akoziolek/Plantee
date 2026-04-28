package com.example.plantee.ui.components.base

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.plantee.R

@Composable
fun DeleteConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    message: String,
    confirmText: String,
    dismissText: String
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = dismissText)
            }
        }
    )
}

@Composable
fun DateRangePickerModal(
    state: DateRangePickerState,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.input_label_date_picker),
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            state.selectedStartDateMillis,
                            state.selectedEndDateMillis
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(
            state = state,
            title = {
                Text(
                    text = "Select date range"
                )
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        )
    }

}
