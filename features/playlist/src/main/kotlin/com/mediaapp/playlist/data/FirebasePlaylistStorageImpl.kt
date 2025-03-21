package com.mediaapp.playlist.data

import com.google.firebase.database.DatabaseReference
import com.mediaapp.core.models.PlaylistData
import com.mediaapp.core.models.Track
import kotlinx.coroutines.tasks.await

class FirebasePlaylistStorageImpl(
    private val firebaseDatabase: DatabaseReference,
) : PlaylistStorage {

    override suspend fun createPlaylist(playlistDataModel: PlaylistData) {
        val playlistExists = checkPlaylistExists(playlistDataModel.name)
        if (!playlistExists) {
            firebaseDatabase.child("Playlists").child(playlistDataModel.name)
                .setValue(playlistDataModel).await()
        } else {
            throw IllegalStateException("Плейлист с таким названием уже существует")
        }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistDataModel: PlaylistData) {
        if (checkPlaylistExists(playlistDataModel.name))
            firebaseDatabase.child("Playlists").child(playlistDataModel.name).child("tracks").push()
                .setValue(track).await()
    }

    private suspend fun checkPlaylistExists(playlistName: String): Boolean {
        val snapshot = firebaseDatabase.child("Playlists").child(playlistName).get().await()
        return snapshot.exists()
    }
}
