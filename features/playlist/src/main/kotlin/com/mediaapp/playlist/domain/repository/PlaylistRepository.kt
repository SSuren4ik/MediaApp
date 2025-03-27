package com.mediaapp.playlist.domain.repository

import com.mediaapp.core.models.PlaylistData
import com.mediaapp.core.models.Track

interface PlaylistRepository {

    suspend fun createPlaylist(playlistName: String)

    suspend fun getUserPlaylists(): List<PlaylistData>

    suspend fun updatePlaylistName(playlistNewName: String, playlistOldName: String)

    suspend fun getPlaylistTracks(playlistName: String): PlaylistData

    suspend fun addTrackToPlaylist(playlistName: String, track: Track)
}