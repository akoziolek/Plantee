package com.example.plantee.ui.components.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.ui.theme.PlanteeTheme

private val PrimaryButtonShape = RoundedCornerShape(10.dp)
private val PrimaryButtonElevation = 8.dp

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 58.dp),
        enabled = enabled,
        shape = PrimaryButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = PrimaryButtonElevation)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun PrimaryButtonFullWidth(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    PrimaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled
    )
}
@Composable
fun PrimaryFloatingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = PrimaryButtonShape,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation =  PrimaryButtonElevation),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ButtonsPreview() {
    PlanteeTheme() {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PrimaryButtonFullWidth(text = stringResource(R.string.plant_add_btn_add), onClick = {})
            PrimaryButtonFullWidth(text = stringResource(R.string.plant_edit_btn_save), onClick = {})
            PrimaryButtonFullWidth(text = stringResource(R.string.plant_diagnosis_btn_diagnose), onClick = {})
            PrimaryButtonFullWidth(text = stringResource(R.string.plant_diagnosis_btn_finish), onClick = {})
            PrimaryFloatingButton(text = stringResource(R.string.plant_nav_add), onClick = {})
            PrimaryFloatingButton(text = stringResource(R.string.entry_nav_add), onClick = {})
            PrimaryFloatingButton(text = stringResource(R.string.routine_nav_add), onClick = {})
            PrimaryFloatingButton(text = stringResource(R.string.routine_btn_save), onClick = {})
        }
    }
}