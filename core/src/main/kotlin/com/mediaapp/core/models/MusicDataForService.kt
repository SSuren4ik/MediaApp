package com.mediaapp.core.models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicDataForService(
    val musicName: String,
    val artistName: String,
    val duration: Int,
    val audio: String,
    val image: Bitmap?,
) : Parcelable