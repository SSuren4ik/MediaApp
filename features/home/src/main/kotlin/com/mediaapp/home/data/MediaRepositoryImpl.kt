package com.mediaapp.home.data

import com.mediaapp.core.domain.MediaService
import com.mediaapp.home.domain.models.NetworkRequest
import com.mediaapp.home.domain.models.NewMusic
import com.mediaapp.home.domain.models.PopularMusic
import com.mediaapp.home.domain.models.TopDownloadsMusic
import com.mediaapp.home.domain.repository.MediaRepository

class MediaRepositoryImpl(
    private val service: MediaService,
    private val apiKey: String,
) : MediaRepository {

    override suspend fun getPopularMusic(): NetworkRequest {
        return try {
            val musicData = service.getPopular(apiKey)
            val popularMusic = PopularMusic(musicData.results)
            NetworkRequest.NormalConnect.PopularMusicRequest(popularMusic)
        } catch (e: Exception) {
            NetworkRequest.ErrorConnect("Проблемы с сетью")
        }
    }

    override suspend fun getNewMusic(): NetworkRequest {
        return try {
            val musicData = service.getNewReleases(apiKey)
            val newMusic = NewMusic(musicData.results)
            NetworkRequest.NormalConnect.NewReleasesRequest(newMusic)
        } catch (e: Exception) {
            NetworkRequest.ErrorConnect("Проблемы с сетью")
        }
    }

    override suspend fun getTopDownloadsMusic(): NetworkRequest {
        return try {
            val musicData = service.getTopDownloads(apiKey)
            val topDownloadsMusic = TopDownloadsMusic(musicData.results)
            NetworkRequest.NormalConnect.TopDownloadsRequest(topDownloadsMusic)
        } catch (e: Exception) {
            NetworkRequest.ErrorConnect("Проблемы с сетью")
        }
    }
}