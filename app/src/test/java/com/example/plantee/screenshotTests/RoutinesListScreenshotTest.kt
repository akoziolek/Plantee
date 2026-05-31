package com.example.plantee.screenshotTests

import androidx.compose.foundation.lazy.LazyColumn
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.plantee.domain.model.RoutineSummary
import com.example.plantee.ui.components.shared.todayRoutinesSection
import com.example.plantee.ui.theme.PlanteeTheme
import org.junit.Rule
import org.junit.Test

class RoutinesListScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar"
    )

    @Test
    fun testRoutinesList() {
        paparazzi.snapshot {
            PlanteeTheme {
                LazyColumn {
                    todayRoutinesSection(
                        routines = listOf(
                            RoutineSummary(id = 1L, name = "Routine 1"),
                            RoutineSummary(id = 2L, name = "Routine 2", description = "Short description")
                        ),
                        onCheckboxClick = {  },
                        onItemClick = {  }
                    )
                }
            }
        }
    }

    @Test
    fun testRoutinesList_LongDescription() {
        paparazzi.snapshot( name = "long_description") {
            PlanteeTheme {
                LazyColumn {
                    todayRoutinesSection(
                        routines = listOf(
                            RoutineSummary(id = 1L, name = "Routine 1", description = "This is a very long description. ".repeat(10)),
                            RoutineSummary(id = 2L, name = "Routine 2", description = "This is a very long description. ".repeat(20)),
                        ),
                        onCheckboxClick = {  },
                        onItemClick = {  }
                    )
                }
            }
        }
    }

    @Test
    fun testRoutinesList_Empty() {
        paparazzi.snapshot( name = "empty" ) {
            PlanteeTheme {
                LazyColumn {
                    todayRoutinesSection(
                        routines = listOf(),
                        onCheckboxClick = {  },
                        onItemClick = {  }
                    )
                }
            }
        }
    }


}