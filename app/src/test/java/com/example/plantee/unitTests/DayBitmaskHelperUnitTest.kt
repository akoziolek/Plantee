package com.example.plantee.unitTests

import com.example.plantee.utils.DayBitmaskHelper
import com.example.plantee.utils.DayBitmaskHelper.toDaysList
import org.junit.Assert
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class DayBitmaskHelperUnitTest {
    @Test
    fun `allDaysMask should return 127 which equals to all 7 days selected`() {
        Assert.assertEquals(127, DayBitmaskHelper.allDaysMask())
    }

    @Test
    fun `isSelected should return false when mask is empty`() {
        Assert.assertFalse(DayBitmaskHelper.isSelected(0, DayOfWeek.MONDAY))
    }

    @Test
    fun `isSelected should return true when single corresponding day bit is set`() {
        Assert.assertTrue(DayBitmaskHelper.isSelected(16, DayOfWeek.FRIDAY))
    }

    @Test
    fun `isSelected should correctly evaluate multiple enabled days in a mask`() {
        val mondayAndFridayMask = 17

        Assert.assertTrue(DayBitmaskHelper.isSelected(mondayAndFridayMask, DayOfWeek.MONDAY))
        Assert.assertTrue(DayBitmaskHelper.isSelected(mondayAndFridayMask, DayOfWeek.FRIDAY))
        Assert.assertFalse(DayBitmaskHelper.isSelected(mondayAndFridayMask, DayOfWeek.TUESDAY))
    }

    @Test
    fun `isSelected should set valid mask for out of bounds mask before checking selection`() {
        Assert.assertFalse(DayBitmaskHelper.isSelected(-1, DayOfWeek.MONDAY))
    }

    @Test
    fun `toggleBit should enable day bit when it was previously disabled`() {
        val maskWithFriday = 16
        val expectedMaskWithMondayAndFriday = 17

        Assert.assertEquals(
            expectedMaskWithMondayAndFriday,
            DayBitmaskHelper.toggleBit(maskWithFriday, DayOfWeek.MONDAY)
        )
    }

    @Test
    fun `toggleBit should enable day bit when starting from empty mask`() {
        Assert.assertEquals(1, DayBitmaskHelper.toggleBit(0, DayOfWeek.MONDAY))
    }

    @Test
    fun `toggleBit should disable day bit when it was already enabled`() {
        val maskWithMonTueWed = 7
        val expectedMaskWithMonTue = 3

        Assert.assertEquals(
            expectedMaskWithMonTue,
            DayBitmaskHelper.toggleBit(maskWithMonTueWed, DayOfWeek.WEDNESDAY)
        )
    }

    @Test
    fun `toggleBit should set valid mask for out of bounds mask before toggling`() {
        Assert.assertEquals(126, DayBitmaskHelper.toggleBit(128, DayOfWeek.MONDAY))
    }

    @Test
    fun `selectedDaysCount should return 7 when all days are active`() {
        Assert.assertEquals(7, DayBitmaskHelper.selectedDaysCount(127))
    }

    @Test
    fun `selectedDaysCount should return 0 when mask is empty`() {
        Assert.assertEquals(0, DayBitmaskHelper.selectedDaysCount(0))
    }

    @Test
    fun `selectedDaysCount should return correct count for a valid mask`() {
        val threeDaysMask = 21
        Assert.assertEquals(3, DayBitmaskHelper.selectedDaysCount(threeDaysMask))
    }

    @Test
    fun `selectedDaysCount should cut the mask outside of day range`() {
        Assert.assertEquals(7, DayBitmaskHelper.selectedDaysCount(1247))
    }

    @Test
    fun `toDaysList should return all days when mask represents all days`() {
        Assert.assertEquals(DayOfWeek.entries, DayBitmaskHelper.allDaysMask().toDaysList())
    }

    @Test
    fun `toDaysList should return empty list when mask is 0`() {
        Assert.assertTrue(0.toDaysList().isEmpty())
    }

    @Test
    fun `toDaysList should map mask bits to correct DayOfWeek objects`() {
        val expected = listOf(DayOfWeek.MONDAY, DayOfWeek.SUNDAY)
        val mask = 65

        Assert.assertEquals(expected, mask.toDaysList())
    }

    @Test
    fun `toDayBitMask should return correct single bit for a given date`() {
        with(DayBitmaskHelper) {
            val fridayDate = LocalDate.of(2026, 5, 29)
            Assert.assertEquals(16, fridayDate.toDayBitMask())
        }
    }
}