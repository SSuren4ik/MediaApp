package com.mediaapp.album.data

import com.mediaapp.album.domain.AlbumData
import com.mediaapp.album.domain.AlbumMediaRepository
import com.mediaapp.album.domain.NetworkRequest
import com.mediaapp.core.domain.MediaService

class AlbumMediaRepositoryImpl(
    private val service: MediaService,
    private val apiKey: String,
) : AlbumMediaRepository {

    override suspend fun getAlbumTracks(data: AlbumData): NetworkRequest {
        return try {
            val musicData = service.getAlbumTracks(apiKey, data.albumName)
            NetworkRequest.NormalConnect(musicData.results)
        } catch (e: Exception) {
            NetworkRequest.ErrorConnect("Проблемы с сетью")
        }
    }
}