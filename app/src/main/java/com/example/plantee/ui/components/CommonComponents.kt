package com.example.plantee.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun InputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    supportingText: String? = null,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        supportingText = supportingText?.let { { Text(it)
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall
            )
        } },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onSurface,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedContainerColor = Color.Transparent,
        ),
        singleLine = true
    )
}

@Composable
fun DaysOfWeek(
    selectedDays: List<Int>,
    onDayClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        daysOfWeek.forEachIndexed { index, day ->
            val isSelected = selectedDays.contains(index)

            FilterChip(
                selected = isSelected,
                onClick = { onDayClick(index) },
                label = { Text(day) },
                shape = CircleShape
                )
        }
    }
}

@Composable
fun PlantListItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = {
            Text(
                description,
                maxLines = 2, overflow = TextOverflow.Visible) },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.outlineVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    state: SearchBarState,
    modifier: Modifier = Modifier,
    placeholder: String = "Search"
) {
    SearchBar(
        state = state,
        inputField = {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text(placeholder) },
                trailingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .safeDrawingPadding(),
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        shape = SearchBarDefaults.inputFieldShape,
        tonalElevation = SearchBarDefaults.TonalElevation,
        shadowElevation = SearchBarDefaults.ShadowElevation,
    )
}