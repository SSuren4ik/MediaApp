package com.mediaapp.playlist.domain.usecase

import com.mediaapp.core.models.PlaylistData
import com.mediaapp.playlist.domain.repository.PlaylistRepository

class GetUserPlaylistsUseCase(private val playlistRepository: PlaylistRepository) {

    suspend fun execute(): List<PlaylistData> {
        return playlistRepository.getUserPlaylists()
    }
}