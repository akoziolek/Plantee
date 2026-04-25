package com.example.plantee.ui.components.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.ui.components.base.InputTextField
import com.example.plantee.ui.theme.PlanteeTheme

@Composable
fun PlantFormFields(
    nameValue: String,
    onNameChange: (String) -> Unit,
    speciesValue: String,
    onSpeciesChange: (String) -> Unit,
    descriptionValue: String,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    nameError: Boolean = false
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        InputTextField(
            title = stringResource(R.string.plant_form_label_plant),
            value = nameValue,
            onValueChange = onNameChange,
            supportingText = stringResource(R.string.plant_form_support_plant),
            isError = nameError,
            errorText = stringResource(R.string.plant_form_error_name)
        )
        InputTextField(
            title = stringResource(R.string.plant_form_label_species),
            value = speciesValue,
            onValueChange = onSpeciesChange,
            supportingText = stringResource(R.string.plant_form_support_species)
        )
        InputTextField(
            title = stringResource(R.string.plant_form_label_description),
            value = descriptionValue,
            onValueChange = onDescriptionChange,
            supportingText = stringResource(R.string.plant_form_support_description),
            singleLine = false,
            minLines = 3
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PlantFormFieldsPreview() {
    PlanteeTheme {
        PlantFormFields(
            "My favourite plant",
            {},
            "Philodendron",
            {},
            "Plant is not looking very good, I am very worried :(((",
            {}
        )
    }
}
