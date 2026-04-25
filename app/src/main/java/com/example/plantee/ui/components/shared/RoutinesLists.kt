package com.example.plantee.ui.components.shared

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
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

fun LazyListScope.routinesSection(
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


