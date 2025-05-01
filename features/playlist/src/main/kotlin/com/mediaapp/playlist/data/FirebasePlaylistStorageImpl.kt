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
        val userId = firebaseAuth.currentUser!!.uid
        checkPlaylistExistsForUser(
            playlistName, userId
        )

        val userName = getUserName(userId)

        val playlistRef = firebaseDatabase.child("Playlists").push()
        val playlistId = playlistRef.key!!

        val playlistData = PlaylistData(
            playlistId = playlistId,
            playlistName = playlistName,
            authorId = userId,
            authorName = userName,
            owners = listOf(userId)
        )

        playlistRef.setValue(playlistData).await()

        firebaseDatabase.child("Users").child(userId).child("playlists").push().setValue(playlistId)
            .await()
    }

    override suspend fun getUserPlaylists(): List<PlaylistData> {
        val currentUser = firebaseAuth.currentUser!!
        val userPlaylistsRef =
            firebaseDatabase.child("Users").child(currentUser.uid).child("playlists")
        val snapshot = userPlaylistsRef.get().await()

        val playlists = mutableListOf<PlaylistData>()
        for (playlistSnapshot in snapshot.children) {
            val playlistId = playlistSnapshot.getValue(String::class.java) ?: continue
            val playlistRef = firebaseDatabase.child("Playlists").child(playlistId)
            val playlistSnapshot = playlistRef.get().await()
            playlistSnapshot.getValue(PlaylistData::class.java)?.let { playlists.add(it) }
        }
        return playlists
    }

    override suspend fun updatePlaylistName(playlistId: String, playlistNewName: String) {
        val playlistRef = firebaseDatabase.child("Playlists").child(playlistId)

        val snapshot = playlistRef.get().await()
        if (!snapshot.exists()) {
            throw FirebaseExceptions.PlaylistNotFoundException("Плейлист не найден")
        }

        playlistRef.child("playlistName").setValue(playlistNewName).await()
    }

    override suspend fun getPlaylistTracks(playlistId: String): PlaylistData {
        val playlistRef = firebaseDatabase.child("Playlists").child(playlistId)
        val snapshot = playlistRef.get().await()
        return snapshot.getValue(PlaylistData::class.java)
            ?: throw FirebaseExceptions.PlaylistNotFoundException("Плейлист не найден")
    }

    override suspend fun addTrackToPlaylist(playlistId: String, track: Track) {
        val playlistRef = firebaseDatabase.child("Playlists").child(playlistId)
        val snapshot = playlistRef.get().await()
        val playlist = snapshot.getValue(PlaylistData::class.java)
            ?: throw FirebaseExceptions.PlaylistNotFoundException("Плейлист не найден")

        val tracks = playlist.tracks.toMutableList()
        if (tracks.any { it.id == track.id }) {
            throw FirebaseExceptions.TrackAlreadyExistsException("Трек уже добавлен в плейлист")
        }
        tracks.add(track)
        playlistRef.child("tracks").setValue(tracks).await()
    }

    override suspend fun getSelectedUserPlaylists(userId: String): List<PlaylistData> {
        val userPlaylistsRef = firebaseDatabase.child("Users").child(userId).child("playlists")
        val snapshot = userPlaylistsRef.get().await()

        val playlists = mutableListOf<PlaylistData>()
        for (playlistSnapshot in snapshot.children) {
            val playlistId = playlistSnapshot.getValue(String::class.java) ?: continue
            val playlistRef = firebaseDatabase.child("Playlists").child(playlistId)
            val playlistSnapshot = playlistRef.get().await()
            playlistSnapshot.getValue(PlaylistData::class.java)?.let { playlists.add(it) }
        }
        return playlists
    }

    override suspend fun saveSelectedUserPlaylist(playlistData: PlaylistData) {
        val currentUserId = firebaseAuth.currentUser!!.uid
        val playlistRef = firebaseDatabase.child("Playlists").child(playlistData.playlistId)
        val snapshot = playlistRef.get().await()

        val playlist = snapshot.getValue(PlaylistData::class.java)
            ?: throw FirebaseExceptions.PlaylistNotFoundException("Плейлист не найден")

        if (playlist.owners.contains(currentUserId)) {
            throw FirebaseExceptions.PlaylistSavedAlreadyException("Вы уже добавили этот плейлист в свою коллекцию")
        }
        val updatedPlaylistData = playlist.copy(
            owners = playlist.owners + currentUserId
        )
        playlistRef.setValue(updatedPlaylistData).await()
        firebaseDatabase.child("Users").child(currentUserId).child("playlists").push()
            .setValue(playlistData.playlistId).await()
    }


    override suspend fun removeTrackFromPlaylist(playlistId: String, track: Track) {
        val playlistRef = firebaseDatabase.child("Playlists").child(playlistId)
        val snapshot = playlistRef.get().await()
        val playlist = snapshot.getValue(PlaylistData::class.java)
            ?: throw FirebaseExceptions.PlaylistNotFoundException("Плейлист не найден")

        val tracks = playlist.tracks.toMutableList()
        if (!tracks.remove(track)) {
            throw FirebaseExceptions.TrackNotFoundException("Трек не найден в плейлисте")
        }
        playlistRef.child("tracks").setValue(tracks).await()
    }
    private suspend fun getUserName(userId: String): String {
        val userRef = firebaseDatabase.child("Users").child(userId).child("username")
        val snapshot = userRef.get().await()
        return snapshot.value as? String ?: ""
    }

    private suspend fun checkPlaylistExistsForUser(playlistName: String, userId: String) {
        val playlistsRef = firebaseDatabase.child("Users").child(userId).child("playlists")
        val snapshot = playlistsRef.get().await()

        for (playlistSnapshot in snapshot.children) {
            val playlistId = playlistSnapshot.getValue(String::class.java) ?: continue
            val playlistRef = firebaseDatabase.child("Playlists").child(playlistId)
            val playlistDataSnapshot = playlistRef.get().await()
            val playlist = playlistDataSnapshot.getValue(PlaylistData::class.java)
            if (playlist?.playlistName == playlistName) {
                throw FirebaseExceptions.PlaylistAlreadyExistsException("У вас уже есть плейлист с таким названием")
            }
        }
    }
}