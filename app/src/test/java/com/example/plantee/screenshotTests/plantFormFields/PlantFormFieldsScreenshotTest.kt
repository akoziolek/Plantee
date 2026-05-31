package com.example.plantee.screenshotTests.plantFormFields

import androidx.compose.material3.Surface
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.plantee.ui.components.shared.PlantFormFields
import com.example.plantee.ui.theme.PlanteeTheme
import org.junit.Rule
import org.junit.Test

class PlantFormFieldsScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar"
    )

    @Test
    fun testPlantsFormFields() {
        paparazzi.snapshot( name = "basic") {
            PlanteeTheme {
                PlantFormFields(
                    "My favourite plant",
                    {},
                    "Philodendron",
                    {},
                    "Plant is not looking very good, I am very worried :(((",
                    {}
                )
            }
        }
    }

    @Test
    fun testPlantFormFields_Empty() {
        paparazzi.snapshot( name = "empty") {
            PlanteeTheme {
                PlantFormFields(
                    "",
                    {},
                    "",
                    {},
                    "",
                    {}
                )
            }
        }
    }

    @Test
    fun testPlantFormFields_ColorPalettes() {
        val themes = listOf(true, false)
        themes.forEach { isDark ->
            val suffix = if (isDark) "dark" else "light"
            paparazzi.snapshot(name = "palette_$suffix") {
                PlanteeTheme(darkTheme = isDark) {
                    Surface {
                        PlantFormFields(
                            "Cactus",
                            {},
                            "Spiky",
                            {},
                            "Testing colors",
                            {}
                        )
                    }
                }
            }
        }
    }

    @Test
    fun testPlantFormFields_NameError() {
        paparazzi.snapshot(name = "dark_mode") {
            PlanteeTheme(darkTheme = true) {
                PlantFormFields(
                    "",
                    {},
                    "Deliciosa",
                    {},
                    "Needs water",
                    {},
                    nameError = true
                )
            }
        }
    }
}