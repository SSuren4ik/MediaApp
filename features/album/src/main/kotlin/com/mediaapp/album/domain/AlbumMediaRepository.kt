package com.mediaapp.album.domain

interface AlbumMediaRepository {
    suspend fun getAlbumTracks(albumName: AlbumData): NetworkRequest
}