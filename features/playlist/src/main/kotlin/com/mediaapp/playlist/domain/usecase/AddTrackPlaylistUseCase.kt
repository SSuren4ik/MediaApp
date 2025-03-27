package com.mediaapp.playlist.domain.usecase

import com.mediaapp.core.models.Track
import com.mediaapp.playlist.domain.models.UserPlaylistsResponseStatusModel
import com.mediaapp.playlist.domain.repository.PlaylistRepository

class AddTrackPlaylistUseCase(private val playlistRepository: PlaylistRepository) {

    suspend fun execute(playlistName: String, track: Track): UserPlaylistsResponseStatusModel {
        playlistRepository.addTrackToPlaylist(playlistName, track)
        return UserPlaylistsResponseStatusModel.Success.SuccessAddSongToPlaylist
    }
}