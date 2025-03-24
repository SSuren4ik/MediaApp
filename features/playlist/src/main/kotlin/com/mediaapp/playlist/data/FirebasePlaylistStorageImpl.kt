package com.mediaapp.playlist.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.mediaapp.core.models.PlaylistData
import kotlinx.coroutines.tasks.await

class FirebasePlaylistStorageImpl(
    private val firebaseDatabase: DatabaseReference,
    private val firebaseAuth: FirebaseAuth,
) : PlaylistStorage {

    override suspend fun createPlaylist(playlistName: String) {
        val playlistExists = checkPlaylistExists(playlistName)
        if (!playlistExists) {
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                val userName = getUserName(userId)
                val playlistData = PlaylistData(
                    playlistName = playlistName, authorId = userId, authorName = userName
                )
                firebaseDatabase.child("Users").child(userId).child("Playlists").push()
                    .setValue(playlistData).await()
            } else {
                throw IllegalStateException("Пользователь не аутентифицирован")
            }
        } else {
            throw IllegalStateException("Плейлист с таким названием уже существует")
        }
    }

    override suspend fun getUserPlaylists(): List<PlaylistData> {
        val currentUser = firebaseAuth.currentUser
        val userPlaylistsRef =
            firebaseDatabase.child("Users").child(currentUser?.uid!!).child("Playlists")
        val snapshot = userPlaylistsRef.get().await()
        val playlists = mutableListOf<PlaylistData>()
        snapshot.children.forEach { playlistSnapshot ->
            val playlist = playlistSnapshot.getValue(PlaylistData::class.java)
            playlist?.let { playlists.add(it) }
        }
        return playlists
    }

    override suspend fun updatePlaylistName(playlistNewName: String, playlistOldName: String) {
        check(!checkPlaylistExists(playlistNewName)) { "Плейлист с таким названием уже существует" }

        val currentUser = firebaseAuth.currentUser ?: error("Пользователь не аутентифицирован")

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
        val currentUser = firebaseAuth.currentUser
        val userPlaylistsRef =
            firebaseDatabase.child("Users").child(currentUser!!.uid).child("Playlists")
        val snapshot =
            userPlaylistsRef.orderByChild("playlistName").equalTo(playlistName).get().await()
        val playlist = snapshot.children.first().getValue(PlaylistData::class.java)
        return playlist ?: error("Плейлист не найден")
    }

    private suspend fun getUserName(userId: String): String {
        val userRef = firebaseDatabase.child("Users").child(userId).child("username")
        val snapshot = userRef.get().await()
        return snapshot.value as? String ?: ""
    }

    private suspend fun checkPlaylistExists(playlistName: String): Boolean {
        val currentUser = firebaseAuth.currentUser
        val userPlaylistsRef =
            firebaseDatabase.child("Users").child(currentUser?.uid!!).child("Playlists")
        val snapshot =
            userPlaylistsRef.orderByChild("playlistName").equalTo(playlistName).get().await()
        return snapshot.exists()
    }
}

