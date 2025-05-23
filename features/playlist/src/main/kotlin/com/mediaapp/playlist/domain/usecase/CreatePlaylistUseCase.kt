package com.mediaapp.playlist.domain.usecase

import com.mediaapp.playlist.domain.models.UserPlaylistsResponseStatusModel
import com.mediaapp.playlist.domain.repository.PlaylistRepository

class CreatePlaylistUseCase(private val playlistRepository: PlaylistRepository) {

    suspend fun execute(playlistName: String): UserPlaylistsResponseStatusModel {
        playlistRepository.createPlaylist(playlistName)
        return UserPlaylistsResponseStatusModel.Success.SuccessCreatePlaylist
    }
}