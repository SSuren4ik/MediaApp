package com.mediaapp.playlist.domain.usecase

import com.mediaapp.playlist.domain.models.CurrentPlaylistResponseStatusModel
import com.mediaapp.playlist.domain.repository.PlaylistRepository

class UpdatePlaylistNameUseCase(private val playlistRepository: PlaylistRepository) {

    suspend fun execute(
        playlistNewName: String,
        playlistOldName: String,
    ): CurrentPlaylistResponseStatusModel {
        playlistRepository.updatePlaylistName(playlistNewName, playlistOldName)
        return CurrentPlaylistResponseStatusModel.Success.SuccessUpdatePlaylistName
    }
}