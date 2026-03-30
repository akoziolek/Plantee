package com.example.plantee.ui.components.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.ui.theme.PlanteeTheme

@Composable
fun RoutinesListItem(
    headlineText: String,
    supportingText: String = "",
    checked: Boolean = false,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    ListItem(
        headlineContent = {
            Text(text = headlineText, style = MaterialTheme.typography.titleMedium)
        },
        supportingContent = if (supportingText.isNotEmpty()) {
            { Text(text = supportingText, style = MaterialTheme.typography.bodyMedium) }
        } else null,
        leadingContent = if (onCheckedChange != null) {
            {
                Checkbox(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                        // FIXME better color pick than this?
                        uncheckedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                )
            }
        } else null,
        trailingContent = {
            Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null)
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    )
}


@Preview
@Composable
fun ListItemsPreview() {
    PlanteeTheme() {
        Column() {
            RoutinesListItem("Weekend watering", "Use small amount of water")
            RoutinesListItem("Weekend watering")
            RoutinesListItem("Weekend watering", "Use small amount of water", onCheckedChange = {})
        }

    }
}