package com.mediaapp.playlist.domain.usecase

import com.mediaapp.core.models.Track
import com.mediaapp.playlist.domain.models.UserPlaylistsResponseStatusModel
import com.mediaapp.playlist.domain.repository.PlaylistRepository

class AddTrackPlaylistUseCase(private val playlistRepository: PlaylistRepository) {

    suspend fun execute(playlistId: String, track: Track): UserPlaylistsResponseStatusModel {
        playlistRepository.addTrackToPlaylist(playlistId, track)
        return UserPlaylistsResponseStatusModel.Success.SuccessAddSongToPlaylist
    }
}