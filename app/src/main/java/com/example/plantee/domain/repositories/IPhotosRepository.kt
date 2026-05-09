package com.example.plantee.domain.repositories

import android.net.Uri

interface IPhotosRepository {
    suspend fun saveImage(uri: Uri): String?
}