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

    override suspend fun updatePlaylistName(playlistNewName: String, playlistOldName: String) {
        playlistStorage.updatePlaylistName(playlistNewName, playlistOldName)
    }

    override suspend fun getPlaylistTracks(playlistName: String): PlaylistData {
        return playlistStorage.getPlaylistTracks(playlistName)
    }

    override suspend fun addTrackToPlaylist(playlistName: String, track: Track) {
        playlistStorage.addTrackToPlaylist(playlistName, track)
    }
}