package com.example.plantee.ui.components.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.ui.theme.PlanteeTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun PlantTag(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun PlantImage(
    name: String?,
    specie: String?,
    state: String?,
    modifier: Modifier = Modifier
) {
    //TODO real image, placeholder when there is no picture
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.outlineVariant)
            .height(220.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            // TODO contrast color depending on the background picture
            if (name != null)
                Text(
                    text = name,
                    color = Color.White,
                    style = MaterialTheme.typography.displayMedium
                )
            if(specie != null)
                Text(
                    text = specie,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            if(state != null)
                // TODO diffferent icons depending on the state
                PlantTag(
                    text = state,
                    icon = Icons.Default.HeartBroken,
                    modifier = Modifier
                        .padding(top = 8.dp)

                )
        }
    }
}

@Composable
@Preview
fun PlantImagePreview() {
    PlanteeTheme() {
        PlantImage(
            "My Plant",
            "Philodendron",
            "Good"
        )
    }
}

