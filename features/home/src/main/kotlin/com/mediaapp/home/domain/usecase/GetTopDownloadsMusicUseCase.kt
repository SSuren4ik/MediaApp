package com.mediaapp.home.domain.usecase

import com.mediaapp.home.domain.models.NetworkRequest
import com.mediaapp.home.domain.repository.MediaRepository

class GetTopDownloadsMusicUseCase(private val repository: MediaRepository) {
    suspend fun execute(): NetworkRequest {
        return repository.getTopDownloadsMusic()
    }
}