package com.example.plantee.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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

@Preview(showBackground = true)
@Composable
fun InputsPreview() {
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
    }

}
