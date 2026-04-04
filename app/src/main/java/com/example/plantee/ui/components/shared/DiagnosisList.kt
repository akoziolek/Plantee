package com.example.plantee.ui.components.shared


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import com.example.plantee.ui.components.base.DiagnosisListItem
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun LazyListScope.diagnosisListItems(
    diagnosis: List<String>,
    onDiagnosisClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    diagnosis.forEachIndexed { index, item ->
        item {
            DiagnosisListItem(
                headlineText = item,
                modifier = modifier,
                supportingText = "Longer description duis aute irure dolor $index",
                onClick = { onDiagnosisClick() }
            )
        }

        if (index < diagnosis.size - 1) {
            item {
                HorizontalDivider(
                    modifier = modifier,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}