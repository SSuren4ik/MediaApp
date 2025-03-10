package com.mediaapp.album.domain

interface AlbumMediaRepository {
    suspend fun getAlbumTracks(data: AlbumData): NetworkRequest
}