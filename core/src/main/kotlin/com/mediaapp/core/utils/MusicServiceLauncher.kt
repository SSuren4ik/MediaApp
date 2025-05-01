package com.mediaapp.core.utils

import android.content.Context
import com.mediaapp.core.models.Track

interface MusicServiceLauncher {
    fun startMusicService(context: Context, track: Track, allTracks: List<Track>)
}