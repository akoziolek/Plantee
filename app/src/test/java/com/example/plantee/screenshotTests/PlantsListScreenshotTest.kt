package com.example.plantee.screenshotTests

import androidx.compose.foundation.lazy.LazyColumn
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.plantee.domain.model.PlantSummary
import com.example.plantee.ui.components.shared.plantListItems
import com.example.plantee.ui.theme.PlanteeTheme
import org.junit.Rule
import org.junit.Test

// ./gradlew recordPaparazziDebug --tests "com.example.plantee.screenshotTests.*"
class PlantsListScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar"
    )

    @Test
    fun testPlantsList() {
        paparazzi.snapshot {
            PlanteeTheme {
                LazyColumn {
                    plantListItems(
                        plants = listOf(
                            PlantSummary(id = 1L, name = "Plant 1", isFavourite = false),
                            PlantSummary(id = 2L, name = "Plant 2", description = "Short description", isFavourite = true)
                        ),
                        onPlantClick = {}
                    )
                }
            }
        }
    }

    @Test
    fun testPlantsList_LongDescription() {
        paparazzi.snapshot {
            PlanteeTheme {
                LazyColumn {
                    plantListItems(
                        plants = listOf(
                            PlantSummary(
                                id = 1L,
                                name = "Plant 2",
                                description = "This is a very long description that should be truncated. There should be only two lines of text visible!!!",
                                isFavourite = false
                            ),
                            PlantSummary(id = 2L, name = "Plant 1", isFavourite = false),
                            PlantSummary(
                                id = 3L,
                                name = "Plant 2",
                                description = "This is a very long description that should be truncated. There should be only two lines of text visible!!!",
                                isFavourite = true)
                        ),
                        onPlantClick = {}
                    )
                }
            }
        }
    }

    @Test
    fun testPlantList_Empty() {
        paparazzi.snapshot {
            PlanteeTheme {
                LazyColumn {
                    plantListItems(
                        plants = listOf(),
                        onPlantClick = {}
                    )
                }
            }
        }
    }

}