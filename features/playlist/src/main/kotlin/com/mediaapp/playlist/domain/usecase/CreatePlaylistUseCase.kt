package com.mediaapp.playlist.domain.usecase

import com.mediaapp.core.models.PlaylistData
import com.mediaapp.playlist.domain.repository.PlaylistRepository

class CreatePlaylistUseCase(private val playlistRepository: PlaylistRepository) {

    suspend fun execute(playlistDataModel: PlaylistData) {
        playlistRepository.createPlaylist(playlistDataModel)
    }
}