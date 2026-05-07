package com.example.plantee.ui.components.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.routine.FilterState
import com.example.plantee.utils.DayBitmaskHelper
import com.example.plantee.utils.RoutineStatus
import java.time.DayOfWeek
import java.time.format.TextStyle

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FilterBottomSheet(
    filterState: FilterState,
    onStatusSelected: (RoutineStatus) -> Unit,
    onDayToggled: (DayOfWeek) -> Unit,
    onSelectAllDays: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(com.example.plantee.R.string.routine_status_filter_label),
                style = MaterialTheme.typography.titleMedium
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RoutineStatus.entries.forEach { status ->
                    FilterChip(
                        selected = filterState.status == status,
                        onClick = { onStatusSelected(status) },
                        label = { Text(status.name) },
                        leadingIcon = if (filterState.status == status) {
                            { Icon(Icons.Default.Check, null, Modifier.size(18.dp)) }
                        } else null
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(com.example.plantee.R.string.routine_weekdays_filter_label),
                    style = MaterialTheme.typography.titleMedium
                )
                TextButton(onClick = onSelectAllDays) {
                    Text(text = stringResource(com.example.plantee.R.string.routine_weekdays_filter_all))
                }
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DayOfWeek.entries.forEach { day ->
                    val isSelected = DayBitmaskHelper.isSelected(filterState.selectedDays, day)

                    FilterChip(
                        selected = isSelected,
                        onClick = { onDayToggled(day) },
                        label = {
                            Text(
                                text = day.getDisplayName(TextStyle.FULL, LocalLocale.current.platformLocale)
                                    .replaceFirstChar { it.uppercase() }
                            )
                        },
                        leadingIcon = if (isSelected) {
                            { Icon(Icons.Default.Check, null, Modifier.size(18.dp)) }
                        } else null
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SheetsPreview() {
    PlanteeTheme() {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            FilterBottomSheet(
                filterState = FilterState(status = RoutineStatus.Active, selectedDays = 6),
                onSelectAllDays = {},
                onDayToggled = {},
                onStatusSelected = {},
                onDismiss = {}
                )
        }
    }
}