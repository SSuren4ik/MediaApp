package com.mediaapp.playlist.domain.usecase

import com.mediaapp.playlist.domain.repository.PlaylistRepository

class UpdatePlaylistNameUseCase(private val playlistRepository: PlaylistRepository) {

    suspend fun execute(playlistNewName: String, playlistOldName: String) {
        playlistRepository.updatePlaylistName(playlistNewName, playlistOldName)
    }
}