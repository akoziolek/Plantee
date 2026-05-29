package com.example.plantee

import com.example.plantee.utils.DayBitmaskHelper
import com.example.plantee.utils.DayBitmaskHelper.toDaysList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class DayBitmaskHelperUnitTest {
    @Test
    fun `allDaysMask should return 127 which equals to all 7 days selected`() {
        assertEquals(127, DayBitmaskHelper.allDaysMask())
    }

    @Test
    fun `isSelected should return false when mask is empty`() {
        assertFalse(DayBitmaskHelper.isSelected(0, DayOfWeek.MONDAY))
    }

    @Test
    fun `isSelected should return true when single corresponding day bit is set`() {
        assertTrue(DayBitmaskHelper.isSelected(16, DayOfWeek.FRIDAY))
    }

    @Test
    fun `isSelected should correctly evaluate multiple enabled days in a mask`() {
        val mondayAndFridayMask = 17

        assertTrue(DayBitmaskHelper.isSelected(mondayAndFridayMask, DayOfWeek.MONDAY))
        assertTrue(DayBitmaskHelper.isSelected(mondayAndFridayMask, DayOfWeek.FRIDAY))
        assertFalse(DayBitmaskHelper.isSelected(mondayAndFridayMask, DayOfWeek.TUESDAY))
    }

    // TODO what do we actually expect here?
    @Test
    fun `isSelected should treat negative mask as THAT IS A GOOD QUESTION`() {
        assertTrue(DayBitmaskHelper.isSelected(-1, DayOfWeek.MONDAY))
    }

    @Test
    fun `toggleBit should enable day bit when it was previously disabled`() {
        val maskWithFriday = 16
        val expectedMaskWithMondayAndFriday = 17

        assertEquals(expectedMaskWithMondayAndFriday, DayBitmaskHelper.toggleBit(maskWithFriday, DayOfWeek.MONDAY))
    }

    @Test
    fun `toggleBit should enable day bit when starting from empty mask`() {
        assertEquals(1, DayBitmaskHelper.toggleBit(0, DayOfWeek.MONDAY))
    }

    @Test
    fun `toggleBit should disable day bit when it was already enabled`() {
        val maskWithMonTueWed = 7
        val expectedMaskWithMonTue = 3

        assertEquals(expectedMaskWithMonTue, DayBitmaskHelper.toggleBit(maskWithMonTueWed, DayOfWeek.WEDNESDAY))
    }

    // TODO what do we expect here to happen?
    @Test
    fun `toggleBit should clear out of bounds garbage bits before toggling`() {
        assertEquals(1, DayBitmaskHelper.toggleBit(128, DayOfWeek.MONDAY))
    }

    @Test
    fun `selectedDaysCount should return 7 when all days are active`() {
        assertEquals(7, DayBitmaskHelper.selectedDaysCount(127))
    }

    @Test
    fun `selectedDaysCount should return 0 when mask is empty`() {
        assertEquals(0, DayBitmaskHelper.selectedDaysCount(0))
    }

    @Test
    fun `selectedDaysCount should return correct count for a valid mask`() {
        val threeDaysMask = 21
        assertEquals(3, DayBitmaskHelper.selectedDaysCount(threeDaysMask))
    }

    // TODO what do we expect here?
    @Test
    fun `selectedDaysCount should ignore bits outside of day range`() {
        assertEquals(6, DayBitmaskHelper.selectedDaysCount(1247))
    }

    @Test
    fun `toDaysList should return all days when mask represents all days`() {
        assertEquals(DayOfWeek.entries, DayBitmaskHelper.allDaysMask().toDaysList())
    }

    @Test
    fun `toDaysList should return empty list when mask is 0`() {
        assertTrue(0.toDaysList().isEmpty())
    }

    @Test
    fun `toDaysList should map mask bits to correct DayOfWeek objects`() {
        val expected = listOf(DayOfWeek.MONDAY, DayOfWeek.SUNDAY)
        val mask = 65

        assertEquals(expected, mask.toDaysList())
    }

    @Test
    fun `toDayBitMask should return correct single bit for a given date`() {
        with(DayBitmaskHelper) {
            val fridayDate = LocalDate.of(2026, 5, 29)
            assertEquals(16, fridayDate.toDayBitMask())
        }
    }
}