package com.example.plantee.utils

import java.time.DayOfWeek
import java.time.LocalDate

object DayBitmaskHelper {
    private fun DayOfWeek.toBit(): Int = 1 shl (this.value - 1)

    fun isSelected(mask: Int, day: DayOfWeek): Boolean {
        val checkedMask: Int = if (mask > 127) {
            127
        } else if (mask < 0) {
            0
        } else {
            mask
        }
        return (checkedMask and day.toBit()) != 0
    }

    fun toggleBit(mask: Int, day: DayOfWeek): Int {
        val checkedMask: Int = if (mask > 127) {
            127
        } else if (mask < 0) {
            0
        } else {
            mask
        }
        val bit = day.toBit()
        return checkedMask xor bit
    }

    fun allDaysMask(): Int = (1 shl 7) - 1

    fun selectedDaysCount(mask: Int): Int {;
        val checkedMask: Int = if (mask > 127) {
            127
        } else if (mask < 0) {
            0
        } else {
            mask
        }
        return Integer.bitCount(checkedMask)
    }

    fun LocalDate.toDayBitMask(): Int {
        return 1 shl (this.dayOfWeek.value - 1)
    }

    fun Int.toDaysList(): List<DayOfWeek> {
       return DayOfWeek.entries.filter { isSelected(this, it) }
    }
}

