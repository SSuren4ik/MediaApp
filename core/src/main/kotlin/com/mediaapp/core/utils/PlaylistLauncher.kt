package com.mediaapp.core.utils

import android.content.Context

interface PlaylistLauncher {
    fun launchPlaylist(playlistId: String, context: Context)
}