package com.example.plantee.screenshotTests.plantFormFields

import androidx.compose.material3.Surface
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.plantee.ui.components.shared.PlantFormFields
import com.example.plantee.ui.theme.PlanteeTheme
import org.junit.Rule
import org.junit.Test

class PlantFormFieldsNarrowScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5.copy(screenWidth = 520)
    )

    @Test
    fun testPlantFormFields() {
        paparazzi.snapshot( name = "base") {
            PlanteeTheme {
                Surface {
                    PlantFormFields(
                        "Cactus",
                        {},
                        "Spiky",
                        {},
                        "Small description",
                        {}
                    )
                }
            }
        }
    }

    @Test
    fun testPlantFormFields_LongTexts() {
        paparazzi.snapshot( name = "base") {
            PlanteeTheme {
                Surface {
                    PlantFormFields(
                        "Cactus ".repeat(3),
                        {},
                        "Spiky ".repeat(2),
                        {},
                        "Long description ".repeat(12),
                        {}
                    )
                }
            }
        }
    }
}