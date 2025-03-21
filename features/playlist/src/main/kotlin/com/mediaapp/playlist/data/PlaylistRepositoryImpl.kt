package com.mediaapp.playlist.data

import com.mediaapp.core.models.PlaylistData
import com.mediaapp.core.models.Track
import com.mediaapp.playlist.domain.repository.PlaylistRepository

class PlaylistRepositoryImpl(
    private val playlistStorage: PlaylistStorage,
) : PlaylistRepository {

    override suspend fun createPlaylist(playlistDataModel: PlaylistData) {
        playlistStorage.createPlaylist(playlistDataModel)
    }

    override suspend fun addTrackToPlaylist(track: Track, userDataModel: PlaylistData) {
        playlistStorage.addTrackToPlaylist(track, userDataModel)
    }
}