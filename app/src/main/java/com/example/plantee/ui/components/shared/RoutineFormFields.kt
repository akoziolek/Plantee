package com.example.plantee.ui.components.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.ui.components.base.DateRangeField
import com.example.plantee.ui.components.base.DaysOfWeek
import com.example.plantee.ui.components.base.InputTextField
import com.example.plantee.ui.theme.PlanteeTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineFormFields(
    nameValue: String,
    onNameChange: (String) -> Unit,
    descriptionValue: String,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    nameError: Boolean = false
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InputTextField(
            title = stringResource(R.string.routine_add_label_name),
            value = nameValue,
            onValueChange = onNameChange,
            supportingText = stringResource(R.string.routine_add_support_name),
            isError = nameError,
            errorText = stringResource(R.string.routine_form_error_name),
            maxLength = 35
        )
        InputTextField(
            title = stringResource(R.string.routine_add_label_description),
            value = descriptionValue,
            onValueChange = onDescriptionChange,
            supportingText = stringResource(R.string.routine_add_support_description)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDateFields(
    startDateValue: LocalDate?,
    endDateValue: LocalDate?,
    onDateChange: (Pair<Long?, Long?>) -> Unit,
    activeDaysValue: Int,
    onActiveDaysChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DaysOfWeek(
            selectedDays = activeDaysValue,
            onDayClick = onActiveDaysChange
        )
        DateRangeField(
            startDate = startDateValue,
            endDate = endDateValue,
            onDateRangeSelected = onDateChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun RoutineFormFieldsPreview() {
    PlanteeTheme {
        Column() {
            RoutineFormFields(
                nameValue = "My routine",
                onNameChange = {},
                descriptionValue = "Description",
                onDescriptionChange = {},
            )
            RoutineDateFields(
                startDateValue = LocalDate.now(),
                endDateValue = LocalDate.now(),
                onDateChange = {},
                activeDaysValue = 96,
                onActiveDaysChange = {}
            )
        }
    }
}
