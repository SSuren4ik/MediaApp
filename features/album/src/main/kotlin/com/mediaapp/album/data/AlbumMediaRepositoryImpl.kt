package com.mediaapp.album.data

import com.mediaapp.album.domain.AlbumData
import com.mediaapp.album.domain.AlbumMediaRepository
import com.mediaapp.album.domain.NetworkResponse
import com.mediaapp.core.api.MediaServiceApi

class AlbumMediaRepositoryImpl(
    private val service: MediaServiceApi,
    private val apiKey: String,
) : AlbumMediaRepository {

    override suspend fun getAlbumTracks(data: AlbumData): NetworkResponse {
        return try {
            val musicData = service.getAlbumTracksWithLinks(apiKey, data.albumId)
            NetworkResponse.Success(musicData.results)
        } catch (e: Exception) {
            NetworkResponse.ErrorConnect("Проблемы с сетью")
        }
    }
}