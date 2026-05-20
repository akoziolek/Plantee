package com.example.plantee.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import java.io.ByteArrayOutputStream

object ImageConverter {

    suspend fun uriToOptimizedBase64(context: Context, uri: Uri): String? {
        val request = ImageRequest.Builder(context)
            .data(uri)
            .size(1024)
            .allowConversionToBitmap(true)
            .build()

        val result = ImageLoader(context).execute(request)
        if(result !is SuccessResult) return null
        val bitmap = result.drawable.toBitmap()

        val outPutStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outPutStream)

        return Base64.encodeToString(outPutStream.toByteArray(), Base64.NO_WRAP)
    }
}