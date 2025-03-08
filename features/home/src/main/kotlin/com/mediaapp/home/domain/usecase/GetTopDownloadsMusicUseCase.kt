package com.mediaapp.home.domain.usecase

import com.mediaapp.home.domain.models.NetworkRequest
import com.mediaapp.home.domain.repository.HomeMediaRepository

class GetTopDownloadsMusicUseCase(private val repository: HomeMediaRepository) {
    suspend fun execute(): NetworkRequest {
        return repository.getTopDownloadsMusic()
    }
}