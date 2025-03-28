package com.mediaapp.playlist.data

import com.mediaapp.core.models.PlaylistData
import com.mediaapp.core.models.Track

interface PlaylistStorage {

    suspend fun createPlaylist(playlistName: String)

    suspend fun getUserPlaylists(): List<PlaylistData>

    suspend fun updatePlaylistName(playlistId: String, playlistNewName: String)

    suspend fun getPlaylistTracks(playlistId: String): PlaylistData

    suspend fun addTrackToPlaylist(playlistId: String, track: Track)

    suspend fun getSelectedUserPlaylists(userId: String): List<PlaylistData>

    suspend fun saveSelectedUserPlaylist(playlistData: PlaylistData)
}