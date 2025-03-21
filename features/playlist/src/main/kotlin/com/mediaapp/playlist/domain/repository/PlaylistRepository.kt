package com.mediaapp.playlist.domain.repository

import com.mediaapp.core.models.PlaylistData
import com.mediaapp.core.models.Track

interface PlaylistRepository {

    suspend fun createPlaylist(playlistDataModel: PlaylistData)

    suspend fun addTrackToPlaylist(track: Track, userDataModel: PlaylistData)
}