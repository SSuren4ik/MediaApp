package com.mediaapp.playlist.domain.repository

import com.mediaapp.core.models.PlaylistData
import com.mediaapp.core.models.Track

interface PlaylistRepository {

    suspend fun createPlaylist(playlistName: String)

    suspend fun getUserPlaylists(): List<PlaylistData>

    suspend fun updatePlaylistName(playlistId: String, playlistNewName: String)

    suspend fun getPlaylistTracks(playlistId: String): PlaylistData

    suspend fun addTrackToPlaylist(playlistId: String, track: Track)

    suspend fun getSelectedUserPlaylists(userId: String): List<PlaylistData>

    suspend fun saveSelectedUserPlaylist(playlistData: PlaylistData)

    suspend fun removeTrackFromPlaylist(playlistId: String, track: Track)
}