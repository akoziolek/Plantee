package com.example.plantee.ui.components.shared

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.domain.model.RoutineSummary
import com.example.plantee.ui.components.base.RoutinesListItem

fun LazyListScope.todayRoutinesSection(
    routines: List<String>, // Instead of string there will be routine object
    onItemClick: (Long) -> Unit
) {
    items(routines.size) { index ->
        RoutinesListItem(
            headlineText = routines[index],
            supportingText = "Description for routine no. $index",
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            onCheckedChange = {},
            // TODO change to routine id
            onClick = { onItemClick(1) }
        )
    }
}

fun LazyListScope.routinesSection_TODELETE(
    routines: List<String>,
    onRoutineClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    items(routines.size) { index ->
        RoutinesListItem(
            headlineText = routines[index],
            supportingText = "Description for routine no. $index",
            // TODO change to routine id
            onClick = { onRoutineClick(1) },
            modifier = modifier
        )
    }
}

fun LazyListScope.routinesSection(
    routines: List<RoutineSummary>,
    onRoutineClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if(routines.isEmpty()) {
        item {
            EmptySectionPlaceholder(
                text = stringResource(R.string.label_no_routines_found),
                modifier = modifier,
                minHeight = 100.dp
            )
        }
    }
    else {
        items(
            items = routines,
            key = { routine -> routine.id }
        ) { routine ->
            RoutinesListItem(
                headlineText = routine.name,
                supportingText = routine.description ?: "",
                onClick = { onRoutineClick(routine.id) },
                modifier = modifier
            )
        }
    }
}


