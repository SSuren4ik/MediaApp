package com.mediaapp.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.mediaapp.core.models.MusicDataForService
import com.mediaapp.core.models.Track
import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.music_service.presentation.MusicService

class MusicServiceLauncherImpl : MusicServiceLauncher {

    override fun startMusicService(context: Context, track: Track) {
        loadImageUrlToBitmap(context, track.album_image) { bitmap ->
            val musicDataForService = MusicDataForService(
                musicName = track.name,
                artistName = track.artist_name,
                duration = track.duration,
                audio = track.audio,
                image = bitmap
            )

            val intent = Intent(context, MusicService::class.java).apply {
                action = MusicService.Actions.PLAY.name
                putExtra("track", musicDataForService)
            }

            context.startService(intent)
        }
    }

    private fun loadImageUrlToBitmap(
        context: Context,
        urlString: String,
        callback: (Bitmap?) -> Unit,
    ) {
        Glide.with(context).asBitmap().load(urlString).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                callback(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                callback(null)
            }
        })
    }
}
