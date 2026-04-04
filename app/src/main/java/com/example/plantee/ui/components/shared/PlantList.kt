package com.example.plantee.ui.components.shared

import androidx.compose.foundation.lazy.LazyListScope
import com.example.plantee.ui.components.base.PlantListItem

fun LazyListScope.plantListItems(
    plants: List<String>,
    onPlantClick: () -> Unit
) {
    items(plants.size) { index ->
        PlantListItem(
            title = plants[index],
            description = "Longer description duis aute irure dolor in reprehenderit in voluptate velit of plant no. $index",
            onClick = { onPlantClick() }
        )
    }
}