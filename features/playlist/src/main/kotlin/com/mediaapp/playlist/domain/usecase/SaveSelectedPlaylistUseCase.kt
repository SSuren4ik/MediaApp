package com.mediaapp.playlist.domain.usecase

import com.mediaapp.core.models.PlaylistData
import com.mediaapp.playlist.domain.repository.PlaylistRepository

class SaveSelectedPlaylistUseCase(private val repository: PlaylistRepository) {

    suspend fun execute(playlistData: PlaylistData) {
        repository.saveSelectedUserPlaylist(playlistData)
    }
}