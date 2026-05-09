package com.example.plantee.di

import com.example.plantee.data.repositories.DiagnosesRepository
import com.example.plantee.data.repositories.MediaRepository
import com.example.plantee.data.repositories.PhotosRepository
import com.example.plantee.data.repositories.PlantsRepository
import com.example.plantee.data.repositories.RoutinesRepository
import com.example.plantee.domain.repositories.IDiagnosesRepository
import com.example.plantee.domain.repositories.IMediaRepository
import com.example.plantee.domain.repositories.IPhotosRepository
import com.example.plantee.domain.repositories.IPlantsRepository
import com.example.plantee.domain.repositories.IRoutinesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPlantsRepository(
        plantsRepository: PlantsRepository
    ): IPlantsRepository

    @Binds
    @Singleton
    abstract fun bindDiagnosesRepository(
        diagnosesRepository: DiagnosesRepository
    ): IDiagnosesRepository

    @Binds
    @Singleton
    abstract fun bindRoutinesRepository(
        routinesRepository: RoutinesRepository
    ): IRoutinesRepository

    @Binds
    @Singleton
    abstract fun bindMediaRepository(
        mediaRepository: MediaRepository
    ): IMediaRepository

    @Binds
    @Singleton
    abstract fun bindPhotosRepository(
        photosRepository: PhotosRepository
    ): IPhotosRepository
}
