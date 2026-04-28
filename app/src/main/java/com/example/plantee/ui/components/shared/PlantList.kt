package com.example.plantee.ui.components.shared

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.plantee.R
import com.example.plantee.domain.model.PlantSummary
import com.example.plantee.ui.components.base.PlantListItem

fun LazyListScope.plantListItems_TODELETE(
    plants: List<String>,
    onPlantClick: (Long) -> Unit
) {
    items(plants.size) { index ->
        PlantListItem(
            title = plants[index],
            description = "Longer description duis aute irure dolor in reprehenderit in voluptate velit of plant no. $index",
            // TODO change to plant id
            onClick = { onPlantClick(1) }
        )
    }
}

fun LazyListScope.plantListItems(
    plants: List<PlantSummary>,
    onPlantClick: (Long) -> Unit,
    onPlantBookmarkClick: (Long) -> Unit,
    selectedPlantIds: List<Long> = emptyList()
) {
    if (plants.isEmpty()) {
        item {
            EmptySectionPlaceholder(
                text = stringResource(R.string.label_no_plants_found),
                modifier = Modifier.fillParentMaxSize()
            )
        }
    } else {
        items(
            items = plants,
            key = { plant -> plant.id }
        ) { plant ->
            PlantListItem(
                title = plant.name,
                description = plant.description ?: "",
                isBookmarked = plant.isFavourite,
                isSelected = selectedPlantIds.contains(plant.id),
                onClick = { onPlantClick(plant.id) },
                onBookmarkClick = { onPlantBookmarkClick(plant.id) }
            )
        }
    }
}
