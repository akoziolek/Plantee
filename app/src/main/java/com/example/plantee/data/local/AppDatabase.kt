package com.example.plantee.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.plantee.data.local.converters.DateConverter
import com.example.plantee.data.local.dao.DiagnosisDao
import com.example.plantee.data.local.dao.MediaDao
import com.example.plantee.data.local.dao.PlantRoutinesDao
import com.example.plantee.data.local.dao.PlantsDao
import com.example.plantee.data.local.dao.RoutineSourcesDao
import com.example.plantee.data.local.dao.RoutinesStatisticsDao
import com.example.plantee.data.local.dao.RoutinesDao
import com.example.plantee.data.local.entities.DiagnosisEntity
import com.example.plantee.data.local.entities.MediaEntity
import com.example.plantee.data.local.entities.PlantEntity
import com.example.plantee.data.local.entities.PlantRoutineEntity
import com.example.plantee.data.local.entities.RoutineEntity
import com.example.plantee.data.local.entities.RoutineSourceEntity
import com.example.plantee.data.local.entities.RoutinesStatisticsEntity
import com.example.plantee.data.local.views.DiagnosisSummaryView
import com.example.plantee.data.local.views.PlantSummaryView

@Database(
    entities = [
        DiagnosisEntity::class,
        MediaEntity::class,
        PlantEntity::class,
        PlantRoutineEntity::class,
        RoutineEntity::class,
        RoutineSourceEntity::class,
        RoutinesStatisticsEntity::class
    ],
    views = [
        DiagnosisSummaryView::class,
        PlantSummaryView::class
    ],
    version = 13
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diagnosisDao(): DiagnosisDao
    abstract fun mediaDao(): MediaDao
    abstract fun plantsDao(): PlantsDao
    abstract fun plantRoutinesDao(): PlantRoutinesDao
    abstract fun routinesDao(): RoutinesDao
    abstract fun routineSourcesDao(): RoutineSourcesDao
    abstract fun routinesStatisticsDao(): RoutinesStatisticsDao
}
