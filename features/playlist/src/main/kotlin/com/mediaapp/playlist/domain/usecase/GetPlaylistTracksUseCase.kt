package com.mediaapp.playlist.domain.usecase

import com.mediaapp.core.models.PlaylistData
import com.mediaapp.playlist.domain.repository.PlaylistRepository

class GetPlaylistTracksUseCase(private val repository: PlaylistRepository) {

    suspend fun execute(playlistId: String): PlaylistData {
        return repository.getPlaylistTracks(playlistId)
    }

}