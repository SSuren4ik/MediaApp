package com.mediaapp.playlist.domain.models

import com.mediaapp.core.models.PlaylistData

sealed class CurrentPlaylistResponseStatusModel {
    sealed class Success : CurrentPlaylistResponseStatusModel() {
        data class SuccessGetPlaylist(val data: PlaylistData) : Success()
        data object SuccessUpdatePlaylistName : Success()
        data object SuccessAddSongToPlaylist : Success()
    }

    data class Error(val message: String) : CurrentPlaylistResponseStatusModel()
}