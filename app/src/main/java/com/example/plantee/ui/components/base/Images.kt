package com.example.plantee.ui.components.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.plantee.ui.theme.PlanteeTheme

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
    imagePath: String?,
    name: String?,
    specie: String?,
    state: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.outlineVariant)
            .height(220.dp)
            .fillMaxWidth()
    ) {
        if (!imagePath.isNullOrEmpty()) {
            AsyncImage(
                model = imagePath,
                contentDescription = name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
            }
        }

        // TODO contrast gradient depending on the background picture?
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 200f
                    )
                )
        )

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
            if (specie != null)
                Text(
                    text = specie,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            if(state != null)
                // TODO different icons depending on the state
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
fun PlainImage(
    imagePath: String?,
    name: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.outlineVariant)
            .height(220.dp)
            .fillMaxWidth()
    ) {
        if (!imagePath.isNullOrEmpty()) {
            AsyncImage(
                model = imagePath,
                contentDescription = name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
            }
        }
    }
}

@Composable
fun SmallPlantImage(
    imagePath: String?,
    name: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (!imagePath.isNullOrEmpty()) {
            AsyncImage(
                model = imagePath,
                contentDescription = name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SmallDiagnosisImage(
    imagePath: String?,
    name: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(MaterialTheme.colorScheme.outlineVariant),
//            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (!imagePath.isNullOrEmpty()) {
            AsyncImage(
                model = imagePath,
                contentDescription = name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
@Preview
fun ImagePreview() {
    PlanteeTheme {
        PlantImage(
            imagePath = null,
            name = "My Plant",
            specie = "Philodendron",
            state = "Good"
        )
        PlainImage(
            imagePath = null,
            name = "My Plant"
        )
    }
}
