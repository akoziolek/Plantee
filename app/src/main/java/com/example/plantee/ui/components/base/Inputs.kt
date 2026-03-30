package com.example.plantee.ui.components.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.ui.theme.PlanteeTheme

@Composable
fun InputTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
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
            supportingText = supportingText?.let { text ->
                {
                    InputSupportingText(text = text)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedContainerColor = Color.Transparent,
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
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    steps: Int = 7
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
    selectedDays: List<Int>,
    onDayClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        daysOfWeek.forEachIndexed { index, day ->
            val isSelected = selectedDays.contains(index)

            FilterChip(
                selected = isSelected,
                onClick = { onDayClick(index) },
                label = { Text(day) },
                shape = CircleShape
            )
        }
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
                selectedDays = listOf(0, 3, 5),
                onDayClick = { }
            )
        }
    }
}
