package com.example.plantee.ui.components.shared

import androidx.compose.foundation.lazy.LazyListScope
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
    onPlantBookmarkClick: (Long) -> Unit
) {
    items(plants.size) { index ->
        val plant = plants[index]
        PlantListItem(
            title = plant.name,
            description = plant.description ?: "",
            isBookmarked = plant.isFavourite,
            onClick = { onPlantClick(plant.id) },
            onBookmarkClick = { onPlantBookmarkClick(plant.id) }
        )
    }
}
