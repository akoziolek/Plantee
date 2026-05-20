package com.example.plantee.utils

import android.content.Context
import android.net.Uri
import androidx.annotation.RawRes
import androidx.core.net.toUri

fun Context.getUri(@RawRes rawId: Int): Uri {
    return "android.resource://$packageName/$rawId".toUri()
}