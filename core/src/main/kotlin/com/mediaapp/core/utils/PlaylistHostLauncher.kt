package com.mediaapp.core.utils

import android.content.Context
import com.mediaapp.core.models.Track

interface PlaylistHostLauncher {
    fun launchPlaylistHost(context: Context, track: Track)
}