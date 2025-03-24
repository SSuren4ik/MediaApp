package com.mediaapp.playlist.data

import com.mediaapp.core.models.PlaylistData

interface PlaylistStorage {

    suspend fun createPlaylist(playlistName: String)

    suspend fun getUserPlaylists(): List<PlaylistData>

    suspend fun updatePlaylistName(playlistNewName: String, playlistOldName: String)

    suspend fun getPlaylistTracks(playlistId: String): PlaylistData
}