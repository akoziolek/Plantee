package com.example.plantee.di

import android.content.Context
import androidx.room.Room
import com.example.plantee.data.local.AppDatabase
import com.example.plantee.data.local.dao.DiagnosisDao
import com.example.plantee.data.local.dao.DiagnosisMediaDao
import com.example.plantee.data.local.dao.MediaDao
import com.example.plantee.data.local.dao.PlantRoutinesDao
import com.example.plantee.data.local.dao.PlantsDao
import com.example.plantee.data.local.dao.RoutineSourcesDao
import com.example.plantee.data.local.dao.RoutinesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "plantee_database"
        ).build()
    }

    @Provides
    fun providePlantsDao(database: AppDatabase): PlantsDao {
        return database.plantsDao()
    }

    @Provides
    fun provideDiagnosisDao(database: AppDatabase): DiagnosisDao {
        return database.diagnosisDao()
    }

    @Provides
    fun provideDiagnosisMediaDao(database: AppDatabase): DiagnosisMediaDao {
        return database.diagnosisMediaDao()
    }

    @Provides
    fun provideMediaDao(database: AppDatabase): MediaDao {
        return database.mediaDao()
    }

    @Provides
    fun providePlantRoutinesDao(database: AppDatabase): PlantRoutinesDao {
        return database.plantRoutinesDao()
    }

    @Provides
    fun provideRoutinesDao(database: AppDatabase): RoutinesDao {
        return database.routinesDao()
    }

    @Provides
    fun provideRoutineSourcesDao(database: AppDatabase): RoutineSourcesDao {
        return database.routineSourcesDao()
    }
}
