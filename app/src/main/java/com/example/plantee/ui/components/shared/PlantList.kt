package com.example.plantee.ui.components.shared

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.domain.model.PlantSummary
import com.example.plantee.ui.components.base.EmptySectionPlaceholder
import com.example.plantee.ui.components.base.PlantListItem

fun LazyListScope.plantListItems(
    plants: List<PlantSummary>,
    onPlantClick: (Long) -> Unit,
    onPlantBookmarkClick: ((Long) -> Unit)? = null,
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
                onBookmarkClick = if (onPlantBookmarkClick != null) {
                    { onPlantBookmarkClick(plant.id) }
                } else null
            )

            if(plants.last() == plant) {
                Spacer(modifier = Modifier.height(62.dp))
            }
        }
    }
}
