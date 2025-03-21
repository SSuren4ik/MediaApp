package com.mediaapp.playlist.data

import com.mediaapp.core.models.PlaylistData
import com.mediaapp.core.models.Track

interface PlaylistStorage {

    suspend fun createPlaylist(playlistDataModel: PlaylistData)

    suspend fun addTrackToPlaylist(track: Track, playlistDataModel: PlaylistData)
}