package com.mediaapp.playlist.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.mediaapp.core.models.FirebaseExceptions
import com.mediaapp.core.models.PlaylistData
import com.mediaapp.core.models.Track
import kotlinx.coroutines.tasks.await

class FirebasePlaylistStorageImpl(
    private val firebaseDatabase: DatabaseReference,
    private val firebaseAuth: FirebaseAuth,
) : PlaylistStorage {

    override suspend fun createPlaylist(playlistName: String) {
        checkPlaylistExists(playlistName)
        val userId = firebaseAuth.currentUser!!.uid
        val userName = getUserName(userId)
        val playlistData = PlaylistData(
            playlistName = playlistName, authorId = userId, authorName = userName
        )
        firebaseDatabase.child("Users").child(userId).child("Playlists").push()
            .setValue(playlistData).await()
    }

    override suspend fun getUserPlaylists(): List<PlaylistData> {
        val currentUser = firebaseAuth.currentUser!!
        val userPlaylistsRef =
            firebaseDatabase.child("Users").child(currentUser.uid).child("Playlists")
        val snapshot = userPlaylistsRef.get().await()
        val playlists = mutableListOf<PlaylistData>()
        snapshot.children.forEach { playlistSnapshot ->
            val playlist = playlistSnapshot.getValue(PlaylistData::class.java)
            playlist?.let { playlists.add(it) }
        }
        return playlists
    }

    override suspend fun updatePlaylistName(playlistNewName: String, playlistOldName: String) {
        checkPlaylistExists(playlistNewName)

        val currentUser = firebaseAuth.currentUser!!

        val userPlaylistsRef =
            firebaseDatabase.child("Users").child(currentUser.uid).child("Playlists")

        val snapshot = userPlaylistsRef.get().await()
        snapshot.children.forEach { playlistSnapshot ->
            val playlist = playlistSnapshot.getValue(PlaylistData::class.java)
            if (playlist?.playlistName == playlistOldName) {
                playlistSnapshot.ref.child("playlistName").setValue(playlistNewName).await()
            }
        }
    }

    override suspend fun getPlaylistTracks(playlistName: String): PlaylistData {
        val currentUser = firebaseAuth.currentUser!!
        val userPlaylistsRef =
            firebaseDatabase.child("Users").child(currentUser.uid).child("Playlists")
        val snapshot =
            userPlaylistsRef.orderByChild("playlistName").equalTo(playlistName).get().await()
        val playlist = snapshot.children.first().getValue(PlaylistData::class.java)
            ?: throw FirebaseExceptions.PlaylistNotFoundException("Плейлист не найден")
        return playlist
    }

    override suspend fun addTrackToPlaylist(playlistName: String, track: Track) {
        val currentUser = firebaseAuth.currentUser!!
        val userPlaylistsRef =
            firebaseDatabase.child("Users").child(currentUser.uid).child("Playlists")

        val snapshot =
            userPlaylistsRef.orderByChild("playlistName").equalTo(playlistName).get().await()
        val playlist = snapshot.children.firstOrNull()?.getValue(PlaylistData::class.java)

        val tracks = playlist!!.tracks.toMutableList()

        if (tracks.any { it.id == track.id }) {
            throw FirebaseExceptions.TrackAlreadyExistsException("Трек уже добавлен в плейлист")
        }
        tracks.add(track)
        userPlaylistsRef.child(snapshot.children.first().key!!).child("tracks").setValue(tracks)
            .await()
    }

    private suspend fun getUserName(userId: String): String {
        val userRef = firebaseDatabase.child("Users").child(userId).child("username")
        val snapshot = userRef.get().await()
        return snapshot.value as? String ?: ""
    }

    private suspend fun checkPlaylistExists(playlistName: String) {
        val currentUser = firebaseAuth.currentUser
        val userPlaylistsRef =
            firebaseDatabase.child("Users").child(currentUser?.uid!!).child("Playlists")
        val snapshot =
            userPlaylistsRef.orderByChild("playlistName").equalTo(playlistName).get().await()
        if (snapshot.exists()) {
            throw FirebaseExceptions.PlaylistAlreadyExistsException("Плейлист с таким названием уже существует")
        }
    }
}

