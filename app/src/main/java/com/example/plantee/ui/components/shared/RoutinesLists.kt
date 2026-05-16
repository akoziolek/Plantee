package com.example.plantee.ui.components.shared

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.model.RoutineSummary
import com.example.plantee.ui.components.base.EmptySectionPlaceholder
import com.example.plantee.ui.components.base.RoutinesListItem
import java.time.LocalDate

fun LazyListScope.todayRoutinesSection(
    routines: List<RoutineSummary>,
    onItemClick: (Long) -> Unit,
    onCheckboxClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (routines.isEmpty()) {
        item {
            EmptySectionPlaceholder(
                text = stringResource(R.string.label_no_routines_found),
                modifier = modifier,
                minHeight = 100.dp
            )
        }
    } else {
        items(
            items = routines,
            key = { routine -> "today_${routine.id}" }
        ) { routine ->
            RoutinesListItem(
                headlineText = routine.name,
                supportingText = routine.description ?: "",
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                checked = routine.lastlyDoneAt == LocalDate.now(),
                onCheckedChange = { onCheckboxClick(routine.id) },
                onClick = { onItemClick(routine.id) }
            )
        }
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
            key = { routine -> "all_${routine.id}" }
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


