package com.mediaapp.playlist.domain.usecase

import com.mediaapp.playlist.domain.models.CurrentPlaylistResponseStatusModel
import com.mediaapp.playlist.domain.repository.PlaylistRepository

class UpdatePlaylistNameUseCase(private val playlistRepository: PlaylistRepository) {

    suspend fun execute(
        playlistId: String,
        playlistNewName: String,
    ): CurrentPlaylistResponseStatusModel {
        playlistRepository.updatePlaylistName(playlistId, playlistNewName)
        return CurrentPlaylistResponseStatusModel.Success.SuccessUpdatePlaylistName
    }
}