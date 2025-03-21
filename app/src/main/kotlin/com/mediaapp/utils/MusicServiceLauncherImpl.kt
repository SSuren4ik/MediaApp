package com.mediaapp.utils

import android.content.Context
import android.content.Intent
import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.music_service.presentation.MusicService

class MusicServiceLauncherImpl : MusicServiceLauncher {
    override fun startMusicService(context: Context, songUrl: String) {
        val intent = Intent(context, MusicService::class.java).apply {
            action = MusicService.Actions.PLAY.name
            putExtra("URI", songUrl)
        }
        context.startService(intent)
    }
}