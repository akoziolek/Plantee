package com.example.plantee.ui.components.base

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.domain.model.AiProposedRoutine
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.utils.DayBitmaskHelper
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.format.TextStyle

@Composable
fun RoutinesListItem(
    headlineText: String,
    modifier: Modifier = Modifier,
    supportingText: String = "",
    checked: Boolean = false,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    onClick: () -> Unit = {}
) {
    var isFirstComposition by remember { mutableStateOf(true) }
    val animScaleX = remember { Animatable(1f) }

    LaunchedEffect(checked) {
        if (isFirstComposition) {
            isFirstComposition = false
            return@LaunchedEffect
        }

        if (checked) {
            launch {
                animScaleX.animateTo(0.96f, spring(stiffness = Spring.StiffnessMediumLow))
                animScaleX.animateTo(1f, spring(stiffness = Spring.StiffnessLow, dampingRatio = 0.5f))
            }
        }
    }


    ListItem(
        headlineContent = {
            Text(
                text = headlineText,
                style = MaterialTheme.typography.titleMedium.copy(
                    textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(0.75f)
            )
        },
        supportingContent = if (supportingText.isNotEmpty()) {
            {
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else null,
        leadingContent = if (onCheckedChange != null) {
            {
                Checkbox(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                        uncheckedColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
        } else null,
        trailingContent = {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null)
        },
        colors = ListItemDefaults.colors(containerColor),
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer(
                scaleX = animScaleX.value
            )
            .heightIn(min = 72.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    )
}

@Composable
fun PlantListItem(
    title: String,
    description: String,
    imagePath: String?,
    modifier: Modifier = Modifier,
    isBookmarked: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    onBookmarkClick: (() -> Unit)? = null
) {
    val scale by animateFloatAsState(
        targetValue = if (isBookmarked) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "ScaleAnimation"
    )
    val bookmarkIcon = if(isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder
    val iconTint = if(isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    val containerColor = if (isSelected) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceVariant

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .heightIn(max = 110.dp),
        color = containerColor
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SmallPlantImage(
                imagePath = imagePath,
                name = title,
                modifier = Modifier.width(100.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(0.75f)
                    )
                    IconButton(
                        onClick = { onBookmarkClick?.invoke() },
                        enabled = onBookmarkClick != null
                    ) {
                        Icon(
                            imageVector = bookmarkIcon,
                            tint = iconTint,
                            contentDescription = "Toggle favourite",
                            modifier = if (onBookmarkClick != null) Modifier.scale(scale) else Modifier
                        )
                    }
                }
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun DiagnosisListItem(
    headlineText: String,
    supportingText: String,
    imagePath: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SmallDiagnosisImage(
            imagePath = imagePath,
            name = stringResource(R.string.diagnosis_photo_name)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = headlineText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(0.75f)
            )
            Text(
                text = supportingText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(0.75f)
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}



@Composable
fun ProposedRoutinesListItem(
    routine: AiProposedRoutine,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    var isFirstComposition by remember { mutableStateOf(true) }
    val animScaleX = remember { Animatable(1f) }

    val start = routine.startDate.orEmpty().ifEmpty { "..." }
    val end = routine.endDate.orEmpty().ifEmpty { "..." }
    val dateText = "$start - $end"

    LaunchedEffect(checked) {
        if (isFirstComposition) {
            isFirstComposition = false
            return@LaunchedEffect
        }

        if (checked) {
            launch {
                animScaleX.animateTo(0.96f, spring(stiffness = Spring.StiffnessMediumLow))
                animScaleX.animateTo(1f, spring(stiffness = Spring.StiffnessLow, dampingRatio = 0.5f))
            }
        }
    }


    ListItem(
        headlineContent = {
            Text(
                text = routine.name,
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            Column {
                if(dateText.isNotEmpty()) {
                    Text(
                        text = dateText,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }

                if (routine.description.isNotEmpty()) {
                    Text(
                        text = routine.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DayOfWeek.entries.forEach { day ->
                        val isSelected = DayBitmaskHelper.isSelected(routine.activeDays, day)
                        if (isSelected) {
                            FilterChip(
                                selected = true,
                                onClick = {},
                                enabled = false,
                                label = {
                                    Text(
                                        text = day.getDisplayName(
                                            TextStyle.SHORT_STANDALONE,
                                            LocalLocale.current.platformLocale
                                        ).replaceFirstChar { it.uppercase() }
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    disabledSelectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    disabledLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                        }
                    }
                }
            }
        },
        leadingContent = {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                    uncheckedColor = MaterialTheme.colorScheme.outline
                )
            )
        },
        colors = ListItemDefaults.colors(containerColor),
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer(
                scaleX = animScaleX.value
            )
            .heightIn(min = 72.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}

@Preview(showBackground = true)
@Composable
fun ListItemsPreview() {
    PlanteeTheme() {
        Column() {
            RoutinesListItem("Weekend watering", supportingText = "Use small amount of water")
            RoutinesListItem("Weekend watering")
            RoutinesListItem("Weekend watering", supportingText = "Use small amount of water", onCheckedChange = {})
            PlantListItem(
                title = "Spider lily",
                description =  "Longer description duis aute irure dolor in reprehenderit in voluptate velit.",
                onClick = { },
                imagePath = null
            )
            PlantListItem(
                title = "Spider lily",
                description =  "Longer description duis aute irure dolor in reprehenderit in voluptate velit.",
                onClick = { },
                imagePath = null
            )
            PlantListItem(
                title = "Spider lily",
                description =  "Longer description duis aute.",
                onClick = { },
                imagePath = null
            )

            DiagnosisListItem(
                headlineText = "28.03.2026",
                supportingText = "Supporting line text lorem ipsum dolor sit amet, consectetur.", imagePath = null)
             ProposedRoutinesListItem(
                AiProposedRoutine(
                    1,
                    "Routine1",
                    "Longer description duis aute irure dolor in reprehenderit in voluptate velit.",
                    58,
                    "2026-01-02",
                    "2026-05-14",
                ), onCheckedChange = {})
        }



    }
}
