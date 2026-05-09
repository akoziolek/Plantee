package com.example.plantee.ui.components.shared


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import com.example.plantee.ui.components.base.DiagnosisListItem
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.domain.model.DiagnosisSummary
import com.example.plantee.ui.components.base.EmptySectionPlaceholder
import java.time.format.DateTimeFormatter


fun LazyListScope.diagnosisListItems(
    diagnoses: List<DiagnosisSummary>,
    onDiagnosisClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if(diagnoses.isEmpty()) {
        item {
            EmptySectionPlaceholder(
                text = stringResource(R.string.label_no_diagnoses_found),
                modifier = modifier,
                minHeight = 100.dp
            )
        }
    } else {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        items(
            items = diagnoses,
            key = { diagnosis -> diagnosis.id }
        ) { diagnosis ->
            DiagnosisListItem(
                headlineText = diagnosis.diagnosedAt.format(formatter),
                modifier = modifier,
                supportingText = diagnosis.description ?: "",
                onClick = { onDiagnosisClick(diagnosis.id) }
            )
            if (diagnoses.last() != diagnosis) {
                HorizontalDivider(
                    modifier = modifier.padding(top = 10.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}