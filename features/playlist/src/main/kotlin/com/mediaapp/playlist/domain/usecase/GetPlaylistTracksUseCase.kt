package com.mediaapp.playlist.domain.usecase

import com.mediaapp.playlist.domain.models.CurrentPlaylistResponseStatusModel
import com.mediaapp.playlist.domain.repository.PlaylistRepository

class GetPlaylistTracksUseCase(private val repository: PlaylistRepository) {

    suspend fun execute(playlistId: String): CurrentPlaylistResponseStatusModel {
        val playlistData = repository.getPlaylistTracks(playlistId)
        return CurrentPlaylistResponseStatusModel.Success.SuccessGetPlaylist(playlistData)
    }

}