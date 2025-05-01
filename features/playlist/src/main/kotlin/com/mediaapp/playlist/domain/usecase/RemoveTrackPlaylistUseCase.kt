package com.mediaapp.playlist.domain.usecase

import com.mediaapp.core.models.Track
import com.mediaapp.playlist.domain.models.CurrentPlaylistResponseStatusModel
import com.mediaapp.playlist.domain.repository.PlaylistRepository

class RemoveTrackPlaylistUseCase(private val playlistRepository: PlaylistRepository) {

    suspend fun execute(playlistId: String, track: Track): CurrentPlaylistResponseStatusModel {
        playlistRepository.removeTrackFromPlaylist(playlistId, track)
        return CurrentPlaylistResponseStatusModel.Success.SuccessRemoveTrackFromPlaylist
    }
}