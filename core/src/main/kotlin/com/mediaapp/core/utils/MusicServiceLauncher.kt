package com.mediaapp.core.utils

import android.content.Context

interface MusicServiceLauncher {
    fun startMusicService(context: Context, songUrl: String)
}