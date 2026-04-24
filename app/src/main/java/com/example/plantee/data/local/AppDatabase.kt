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
import com.example.plantee.data.local.entities.DiagnosisEntity
import com.example.plantee.data.local.entities.DiagnosisMediaEntity
import com.example.plantee.data.local.entities.MediaEntity
import com.example.plantee.data.local.entities.PlantEntity
import com.example.plantee.data.local.entities.PlantRoutineEntity
import com.example.plantee.data.local.entities.RoutineEntity
import com.example.plantee.data.local.entities.RoutineSourceEntity

@Database(
    entities = [
        DiagnosisEntity::class,
        DiagnosisMediaEntity::class,
        MediaEntity::class,
        PlantEntity::class,
        PlantRoutineEntity::class,
        RoutineEntity::class,
        RoutineSourceEntity::class
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