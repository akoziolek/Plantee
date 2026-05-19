package com.example.plantee.ui.components.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.utils.convertLocalDateToDateString
import java.time.LocalDate

@Composable
fun InputTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    isError: Boolean = false,
    errorText: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(modifier = modifier) {
        SectionHeader(
            title = title,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            isError = isError,
            supportingText = {
                if (isError && errorText != null) {
                    Text(
                        text = errorText,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                } else if (supportingText != null) {
                    Text(
                        text = supportingText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedContainerColor = Color.Transparent,
                errorBorderColor = MaterialTheme.colorScheme.errorContainer,
            ),
            singleLine = singleLine,
            minLines = minLines
        )
    }
}

@Composable
fun InputSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 9
) {
    Column(modifier = modifier) {
        SectionHeader(
            title = title,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.height(28.dp),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = steps,
            valueRange = valueRange
        )
    }
}

@Composable
fun DaysOfWeek(
    selectedDays: Int,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.input_label_weekdays),
    onDayClick: ((Int) -> Unit)? = null
) {
    val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")

    Column(modifier = modifier) {
        SectionHeader(title = title)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            daysOfWeek.forEachIndexed { index, day ->
                val isSelected = (selectedDays and (1 shl index)) != 0

                FilterChip(
                    selected = isSelected,
                    onClick = { onDayClick?.invoke(index) },
                    enabled = onDayClick != null,
                    label = { Text(day) },
                    shape = CircleShape
                )
            }
        }
    }
}

@Composable
fun DateRangeField(
    startDate: LocalDate?,
    endDate: LocalDate?,
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    modifier: Modifier = Modifier,
    active: Boolean = true,
    title: String = stringResource(R.string.input_label_date_field),
    fieldText: String = stringResource(R.string.date_field_placeholder),
    modalMessage: String = stringResource(R.string.routine_edit_label_dates_choice),
    confirmText: String = stringResource(R.string.dialog_confirm),
    dismissText: String = stringResource(R.string.dialog_cancel)
) {
    var showModal by remember { mutableStateOf(false) }

    val dateText = if (startDate != null && endDate != null) {
        "${convertLocalDateToDateString(startDate)} - ${convertLocalDateToDateString(endDate)}"
    } else {
        fieldText
    }

    Column(modifier = modifier) {
        SectionHeader(
            title = title,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = dateText,
            supportingText = { Text(stringResource(R.string.date_field_supporting_text)) },
            onValueChange = { },
            readOnly = true,
            label = { Text(stringResource(R.string.input_label_date_field))},
            modifier = modifier
                .fillMaxWidth(),
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            trailingIcon = {
                IconButton(enabled = active, onClick = { if (active) showModal = true }) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null)
                }

            }
        )
    }

    if (showModal) {
        DateRangePickerModal(
            onDateRangeSelected = onDateRangeSelected,
            onDismiss = { showModal = false },
            message = modalMessage,
            confirmText = confirmText,
            dismissText = dismissText,
            startDate = startDate,
            endDate = endDate
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InputsPreview() {
    PlanteeTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InputTextField(
                title = "Name",
                value = "My new Plant",
                onValueChange = {},
                supportingText = "How do you want to name your plant"
            )
            InputTextField(
                title = "Species",
                value = "Philodendron",
                onValueChange = {},
                supportingText = "What is the species of your plant"
            )
            InputTextField(
                title = "Description*",
                value = "Plant is in a very good condition",
                onValueChange = {},
                supportingText = "*Maximum 400 characters",
                singleLine = false,
                minLines = 5
            )
            InputSlider(
                title = "Sun Level",
                value = 10f,
                {}
            )
            DaysOfWeek(
                selectedDays = 12,
                onDayClick = { index -> println("Day $index clicked") }
            )
            DateRangeField(
                onDateRangeSelected = { },
                fieldText = "Choose dates",
                modalMessage = "Pick date range",
                confirmText = "Ok",
                dismissText = "Cancel",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                title = "Date range"
            )
        }
    }
}
