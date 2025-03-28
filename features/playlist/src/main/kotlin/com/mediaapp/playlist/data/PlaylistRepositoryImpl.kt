package com.mediaapp.playlist.data

import com.mediaapp.core.models.PlaylistData
import com.mediaapp.core.models.Track
import com.mediaapp.playlist.domain.repository.PlaylistRepository

class PlaylistRepositoryImpl(
    private val playlistStorage: PlaylistStorage,
) : PlaylistRepository {

    override suspend fun createPlaylist(playlistName: String) {
        playlistStorage.createPlaylist(playlistName)
    }

    override suspend fun getUserPlaylists(): List<PlaylistData> {
        return playlistStorage.getUserPlaylists()
    }

    override suspend fun updatePlaylistName(playlistId: String, playlistNewName: String) {
        playlistStorage.updatePlaylistName(playlistId, playlistNewName)
    }

    override suspend fun getPlaylistTracks(playlistId: String): PlaylistData {
        return playlistStorage.getPlaylistTracks(playlistId)
    }

    override suspend fun addTrackToPlaylist(playlistId: String, track: Track) {
        playlistStorage.addTrackToPlaylist(playlistId, track)
    }

    override suspend fun getSelectedUserPlaylists(userId: String): List<PlaylistData> {
        return playlistStorage.getSelectedUserPlaylists(userId)
    }

    override suspend fun saveSelectedUserPlaylist(playlistData: PlaylistData) {
        playlistStorage.saveSelectedUserPlaylist(playlistData)
    }
}