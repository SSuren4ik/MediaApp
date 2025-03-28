package com.mediaapp.playlist.domain.usecase

import com.mediaapp.playlist.domain.models.UserPlaylistsResponseStatusModel
import com.mediaapp.playlist.domain.repository.PlaylistRepository

class GetSelectedUserPlaylistsUseCase(private val playlistRepository: PlaylistRepository) {

    suspend fun execute(userId: String): UserPlaylistsResponseStatusModel {
        val playlists = playlistRepository.getSelectedUserPlaylists(userId)
        return UserPlaylistsResponseStatusModel.Success.SuccessGetUserPlaylists(playlists)
    }
}