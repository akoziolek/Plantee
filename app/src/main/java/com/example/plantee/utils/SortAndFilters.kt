package com.example.plantee.utils

enum class SortOrder{
    NONE, ASCENDING, DESCENDING;

    fun next(): SortOrder {
        return when (this) {
            NONE -> ASCENDING
            ASCENDING -> DESCENDING
            DESCENDING -> NONE
        }
    }
}

enum class RoutineStatus { Active, All }