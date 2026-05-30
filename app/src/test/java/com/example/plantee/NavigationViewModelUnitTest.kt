package com.example.plantee
import com.example.plantee.ui.nav.NavigationViewModel
import com.example.plantee.ui.nav.Screen
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class NavigationViewModelUnitTest {
    private lateinit var viewModel: NavigationViewModel

    @Before
    fun setUp() {
        viewModel = NavigationViewModel()
    }

    @Test
    fun `initial state should contain Home Screen and show bottom bar`() {
        assertEquals(Screen.Home, viewModel.current())
        assertEquals(1, viewModel.backStack.size)
        assertTrue(viewModel.isBottomBarVisible)
    }

    @Test
    fun `navigate should add screen to back stack`() {
        viewModel.navigate(Screen.Plants)

        assertEquals(Screen.Plants, viewModel.current())
        assertEquals(2, viewModel.backStack.size)
        assertEquals(listOf(Screen.Home, Screen.Plants), viewModel.backStack)
    }

    @Test
    fun `back should remove screen from backstack`() {
        viewModel.navigate(Screen.Plants)
        viewModel.navigate(Screen.Routines)

        viewModel.back()

        assertEquals(Screen.Plants, viewModel.current())
        assertEquals(2, viewModel.backStack.size)
        assertEquals(listOf(Screen.Home, Screen.Plants), viewModel.backStack)
    }

    @Test
    fun `back should not clear the last remaining screen`() {
        viewModel.back()

        assertEquals(Screen.Home, viewModel.current())
        assertEquals(1, viewModel.backStack.size)
    }

    @Test
    fun `replace should change top screen without increasing stack size`() {
        viewModel.navigate(Screen.Plants)
        viewModel.navigate(Screen.Routines)
        viewModel.replace(Screen.Home)

        assertEquals(Screen.Home, viewModel.current())
        assertEquals(3, viewModel.backStack.size)
        assertEquals(listOf(Screen.Home, Screen.Plants, Screen.Home), viewModel.backStack)
    }

    @Test
    fun `replace should change the last screen`() {
        viewModel.replace(Screen.Routines)

        assertEquals(Screen.Routines, viewModel.current())
        assertEquals(1, viewModel.backStack.size)
    }

    @Test
    fun `switchTab should clear stack and set target screen as root`() {
        viewModel.navigate(Screen.Plants)
        viewModel.navigate(Screen.PlantDetails(1L))
        viewModel.navigate(Screen.DiagnosePlant(1L))

        viewModel.switchTab(Screen.Routines)

        assertEquals(Screen.Routines, viewModel.current())
        assertEquals(1, viewModel.backStack.size)
    }

    @Test
    fun `popUpTo non-inclusive should drop everything after target screen`() {
        viewModel.navigate(Screen.Plants)
        viewModel.navigate(Screen.PlantDetails(10L))
        viewModel.navigate(Screen.Routines)

        viewModel.popUpTo(Screen.Plants, inclusive = false)

        assertEquals(Screen.Plants, viewModel.current())
        assertEquals(2, viewModel.backStack.size)
        assertEquals(listOf(Screen.Home, Screen.Plants), viewModel.backStack)
    }

    @Test
    fun `popUpTo inclusive should drop target screen as well`() {
        viewModel.navigate(Screen.Plants)
        viewModel.navigate(Screen.PlantDetails(10L))

        viewModel.popUpTo(Screen.Plants, inclusive = true)

        assertEquals(Screen.Home, viewModel.current())
        assertEquals(1, viewModel.backStack.size)
    }

    @Test
    fun `popUpTo inclusive should not empty the stack completely if target is root`() {
        viewModel.replace(Screen.Plants)
        viewModel.navigate(Screen.Routines)
        viewModel.navigate(Screen.DiagnosePlant(1L))

        viewModel.popUpTo(Screen.Plants, inclusive = true)

        assertEquals(1, viewModel.backStack.size)
        assertEquals(Screen.Plants, viewModel.current())
    }

    @Test
    fun `popUpTo should remove screens to the most recent target instance`() {
        viewModel.navigate(Screen.Plants)
        viewModel.navigate(Screen.PlantDetails(10L))
        viewModel.navigate(Screen.Routines)
        viewModel.navigate(Screen.PlantDetails(11L))
        viewModel.navigate(Screen.Routines)
        viewModel.navigate(Screen.RoutineDetails(1L))

        viewModel.popUpTo(Screen.Routines, inclusive = false)

        assertEquals(Screen.Routines, viewModel.current())
        assertEquals(6, viewModel.backStack.size)
    }


    @Test
    fun `isBottomBarVisible should return false for screens not defined in topLevelScreens`() {
        viewModel.navigate(Screen.PlantAdd)

        assertFalse(viewModel.isBottomBarVisible)
    }


}