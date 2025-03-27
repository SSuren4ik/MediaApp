package com.mediaapp.playlist.data

import com.mediaapp.core.models.PlaylistData
import com.mediaapp.core.models.Track

interface PlaylistStorage {

    suspend fun createPlaylist(playlistName: String)

    suspend fun getUserPlaylists(): List<PlaylistData>

    suspend fun updatePlaylistName(playlistNewName: String, playlistOldName: String)

    suspend fun getPlaylistTracks(playlistName: String): PlaylistData

    suspend fun addTrackToPlaylist(playlistName: String, track: Track)
}