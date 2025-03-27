package com.mediaapp.playlist.domain.usecase

import com.mediaapp.playlist.domain.models.UserPlaylistsResponseStatusModel
import com.mediaapp.playlist.domain.repository.PlaylistRepository

class GetUserPlaylistsUseCase(private val playlistRepository: PlaylistRepository) {

    suspend fun execute(): UserPlaylistsResponseStatusModel {
        val result = playlistRepository.getUserPlaylists()
        return UserPlaylistsResponseStatusModel.Success.SuccessGetUserPlaylists(result)
    }
}