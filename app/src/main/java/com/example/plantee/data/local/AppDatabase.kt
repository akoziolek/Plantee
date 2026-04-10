package com.example.plantee.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.plantee.data.local.converters.DateConverter
import com.example.plantee.data.local.dao.DiagnosisDao
import com.example.plantee.data.local.dao.DiagnosisMediaDao
import com.example.plantee.data.local.dao.MediaDao
import com.example.plantee.data.local.dao.PlantRoutinesDao
import com.example.plantee.data.local.dao.PlantsDao
import com.example.plantee.data.local.dao.RoutineSourcesDao
import com.example.plantee.data.local.dao.RoutinesDao
import com.example.plantee.data.local.entities.Diagnosis
import com.example.plantee.data.local.entities.DiagnosisMedia
import com.example.plantee.data.local.entities.Media
import com.example.plantee.data.local.entities.Plant
import com.example.plantee.data.local.entities.PlantRoutine
import com.example.plantee.data.local.entities.Routine
import com.example.plantee.data.local.entities.RoutineSource

@Database(
    entities = [
        Diagnosis::class,
        DiagnosisMedia::class,
        Media::class,
        Plant::class,
        PlantRoutine::class,
        Routine::class,
        RoutineSource::class
    ],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diagnosisDao(): DiagnosisDao
    abstract fun diagnosisMediaDao(): DiagnosisMediaDao
    abstract fun mediaDao(): MediaDao
    abstract fun plantsDao(): PlantsDao
    abstract fun plantRoutinesDao(): PlantRoutinesDao
    abstract fun routinesDao(): RoutinesDao
    abstract fun routineSourcesDao(): RoutineSourcesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "plantee_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}