package com.example.plantee.ui.components.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.ui.components.base.SectionHeader
import com.example.plantee.ui.theme.PlanteeTheme

@Composable
fun FilterSectionHeader(
    title: String,
    filterTitle: String,
    onClick: () -> Unit
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
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = null,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    // FIXME layout breaks when using R.string, idk why
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
                filterTitle = "Name"
            ) { }

            LinkHeader(
                title = "Routines for today",
                onClick = {}
            )
        }
    }
}