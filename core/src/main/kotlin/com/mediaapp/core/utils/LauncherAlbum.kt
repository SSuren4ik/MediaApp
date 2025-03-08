package com.mediaapp.core.utils

import android.content.Context
import com.mediaapp.core.models.Track

interface LauncherAlbum {
    fun launchAlbum(context: Context, track: Track)
}