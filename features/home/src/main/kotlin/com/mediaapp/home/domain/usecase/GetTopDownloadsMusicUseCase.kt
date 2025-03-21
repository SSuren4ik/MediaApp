package com.mediaapp.home.domain.usecase

import com.mediaapp.home.domain.models.TopDownloadsMusic
import com.mediaapp.home.domain.repository.HomeMediaRepository

class GetTopDownloadsMusicUseCase(private val repository: HomeMediaRepository) {
    suspend fun execute(offset: Int, limit: Int): TopDownloadsMusic {
        return repository.getTopDownloadsMusic(offset, limit)
    }
}