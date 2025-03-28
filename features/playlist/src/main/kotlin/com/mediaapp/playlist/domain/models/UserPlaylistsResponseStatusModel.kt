package com.mediaapp.playlist.domain.models

import com.mediaapp.core.models.PlaylistData

sealed class UserPlaylistsResponseStatusModel {
    sealed class Success : UserPlaylistsResponseStatusModel() {
        data object SuccessCreatePlaylist : Success()
        data class SuccessGetUserPlaylists(val data: List<PlaylistData>) : Success()
        data object SuccessAddSongToPlaylist : Success()
        data object SuccessSaveSelectedPlaylist : Success()
    }

    data class Error(val message: String) : UserPlaylistsResponseStatusModel()
}