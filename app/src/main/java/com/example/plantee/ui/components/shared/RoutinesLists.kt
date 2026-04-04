package com.example.plantee.ui.components.shared

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import com.example.plantee.ui.components.base.RoutinesListItem

fun LazyListScope.todayRoutinesSection(
    routines: List<String>, // Instead of string there will be routine object
    onItemClick: (Int) -> Unit
) {
    items(routines.size) { index ->
        RoutinesListItem(
            headlineText = routines[index],
            supportingText = "Description for routine no. $index",
            // FIXME different color?
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            onCheckedChange = {},
            onClick = { onItemClick(index) }
        )
    }
}

fun LazyListScope.routinesSection(
    routines: List<String>,
    onRoutineClick: () -> Unit
) {
    items(routines.size) { index ->
        RoutinesListItem(
            headlineText = routines[index],
            supportingText = "Description for routine no. $index",
            onClick = onRoutineClick
        )
    }
}


