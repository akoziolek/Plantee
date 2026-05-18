package com.example.plantee.data.repositories

import android.content.Context
import android.net.Uri
import com.example.plantee.domain.repositories.IPhotosRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject


class PhotosRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) : IPhotosRepository {
    override suspend fun saveImage(uri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val imagesDir = File(context.filesDir, "plants")
                if (!imagesDir.exists()) {
                    imagesDir.mkdirs()
                }

                val fileName = "plant_${UUID.randomUUID()}.jpg"
                val file = File(imagesDir, fileName)
                
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                } ?: return@withContext null

                file.absolutePath
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun deleteImage(filePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(filePath)
                if(file.exists()) {
                    file.delete()
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
    }
}
