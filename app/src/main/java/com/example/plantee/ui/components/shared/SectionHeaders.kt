package com.example.plantee.ui.components.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.R
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.ui.components.base.SectionHeader
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.plant.SortOrder

@Composable
fun FilterSectionHeader(
    title: String,
    filterTitle: String,
    onClick: () -> Unit,
    sort: SortOrder
) {
    SectionHeader(
        title = title,
        trailingContent = {
            TextButton(
                onClick = onClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                val icon = when (sort) {
                    SortOrder.NONE -> Icons.AutoMirrored.Filled.Sort
                    SortOrder.ASCENDING -> Icons.Default.ArrowUpward
                    SortOrder.DESCENDING -> Icons.Default.ArrowDownward
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = filterTitle,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    )
}

@Composable
fun LinkHeader(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    SectionHeader(
        title = title,
        trailingContent = {
            IconButton(onClick = onClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, "Go back")
            }
        },
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewSectionHeader() {
    PlanteeTheme() {
        Column() {
            FilterSectionHeader(
                title = "Your plants",
                filterTitle = "Name",
                onClick = { },
                sort = SortOrder.NONE
            )
            LinkHeader(
                title = "Routines for today",
                onClick = {}
            )
        }
    }
}