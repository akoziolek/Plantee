package com.example.plantee.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId

object DayBitmaskHelper {
    private fun DayOfWeek.toBit(): Int = 1 shl (this.value - 1)

    fun isSelected(mask: Int, day: DayOfWeek): Boolean {
        return (mask and day.toBit()) != 0
    }

    fun toggleBit(mask: Int, day: DayOfWeek): Int {
        val bit = day.toBit()
        return mask xor bit
    }

    fun allDaysMask(): Int = (1 shl 7) - 1

    fun selectedDaysCount(mask: Int): Int = Integer.bitCount(mask)

    fun LocalDate.toDayBitMask(): Int {
        return 1 shl (this.dayOfWeek.value - 1)
    }
}

