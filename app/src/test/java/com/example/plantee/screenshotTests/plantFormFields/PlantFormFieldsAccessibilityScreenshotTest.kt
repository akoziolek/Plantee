package com.example.plantee.screenshotTests.plantFormFields

import androidx.compose.material3.Surface
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.plantee.ui.components.shared.PlantFormFields
import com.example.plantee.ui.theme.PlanteeTheme
import org.junit.Rule
import org.junit.Test

class PlantFormFieldsAccessibilityScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5.copy(fontScale = 1.5f)
    )

    @Test
    fun testPlantFormFields_HugeFont() {
        paparazzi.snapshot( name = "huge_font") {
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
}