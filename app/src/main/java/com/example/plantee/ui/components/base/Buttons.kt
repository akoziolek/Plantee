package com.example.plantee.ui.components.base

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.ui.theme.PlanteeTheme

private val PrimaryButtonShape = RoundedCornerShape(10.dp)
private val PrimaryButtonElevation = 8.dp

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 56.dp),
        enabled = enabled,
        shape = PrimaryButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = PrimaryButtonElevation)
    ) {
        if(icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        }
        if(text != null) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        }
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
fun PrimaryIconButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .size(48.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp)
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
            PrimaryButtonFullWidth(text = stringResource(R.string.diagnosis_results_btn_finish), onClick = {})
            PrimaryFloatingButton(text = stringResource(R.string.plant_nav_add), onClick = {})
            PrimaryFloatingButton(text = stringResource(R.string.entry_nav_add), onClick = {})
            PrimaryFloatingButton(text = stringResource(R.string.routine_nav_add), onClick = {})
            PrimaryFloatingButton(text = stringResource(R.string.routine_edit_btn_save), onClick = {})
            PrimaryIconButton(text = stringResource(com.example.plantee.R.string.video_close), icon = Icons.AutoMirrored.Filled.ArrowBack, onClick = {})
        }
    }
}