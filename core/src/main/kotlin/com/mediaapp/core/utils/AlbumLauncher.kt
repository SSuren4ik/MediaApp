package com.mediaapp.core.utils

import android.content.Context
import com.mediaapp.core.models.Track

interface AlbumLauncher {
    fun launchAlbum(context: Context, track: Track)
}