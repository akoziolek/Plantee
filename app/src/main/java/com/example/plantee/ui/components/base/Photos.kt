package com.example.plantee.ui.components.base

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.plantee.R
import com.example.plantee.ui.theme.PlanteeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoPicker(
    selectedUri: Uri?,
    onPhotoSelected: (Uri?) -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        uri -> if(uri != null) {
            onPhotoSelected(uri)
            showSheet = false
        }
    }

    Box(
        modifier = Modifier
            .height(220.dp)
            .fillMaxWidth()
    ) {
        PhotoViewer(selectedUri, canOpenFullScreen = true)
        PrimaryButton(
            icon = if (selectedUri == null) Icons.Default.Add else Icons.Default.Edit,
            onClick = {
                if(selectedUri == null) {
                    launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } else {
                    showSheet = true
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(14.dp)
        )

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                ) {
                    ListItem(
                        headlineContent = { Text(stringResource(R.string.photo_picker_label_pick)) },
                        leadingContent = { Icon(Icons.Default.PhotoLibrary, null) },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        ),
                        modifier = Modifier.clickable {
                            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    )
                    if (selectedUri != null) {
                        ListItem(
                            headlineContent = { Text(stringResource(R.string.photo_picker_label_delete)) },
                            leadingContent = {
                                Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier.clickable {
                                onPhotoSelected(null)
                                showSheet = false
                            }
                        )
                    }
                }
            }
        }
    }


}

@Composable
fun PhotoViewer(
    selectedUri: Uri?,
    canOpenFullScreen: Boolean = false
) {
    // TODO - loading 8k photos
    var isFullScreenVisible by remember { mutableStateOf(false) }

    if(selectedUri != null) {
        AsyncImage(
            model = selectedUri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clickable { isFullScreenVisible = true },
            contentScale = ContentScale.Crop
        )

        if(canOpenFullScreen && isFullScreenVisible) {
            Dialog(
                onDismissRequest = { isFullScreenVisible = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .clickable {isFullScreenVisible = false},
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = selectedUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

    }
    else {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.outlineVariant)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

    }

}

@Preview
@Composable
fun PhotoPickerPreview() {
    PlanteeTheme() {
        PhotoPicker(null,{})
    }
}
