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

    override fun startMusicService(context: Context, track: Track, allTracks: List<Track>) {
        loadImageListToMusicDataList(context, allTracks) { musicDataList ->
            MusicService.MusicPlaylistHolder.trackList = musicDataList
            val index = musicDataList.indexOfFirst { it.audio == track.audio }

            val intent = Intent(context, MusicService::class.java).apply {
                action = MusicService.Actions.PLAY.name
                putExtra("track_index", index)
            }

            context.startService(intent)
        }
    }

    private fun loadImageListToMusicDataList(
        context: Context,
        tracks: List<Track>,
        callback: (List<MusicDataForService>) -> Unit,
    ) {
        val result = mutableListOf<MusicDataForService>()
        var loadedCount = 0

        for (track in tracks) {
            Glide.with(context).asBitmap().load(track.album_image)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?,
                    ) {
                        result.add(
                            MusicDataForService(
                                musicName = track.name,
                                artistName = track.artist_name,
                                duration = track.duration,
                                audio = track.audio,
                                image = resource
                            )
                        )
                        loadedCount++
                        if (loadedCount == tracks.size) {
                            callback(result)
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        loadedCount++
                        if (loadedCount == tracks.size) {
                            callback(result)
                        }
                    }
                })
        }
    }
}
