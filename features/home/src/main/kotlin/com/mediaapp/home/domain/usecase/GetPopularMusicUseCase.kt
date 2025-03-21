package com.mediaapp.home.domain.usecase

import com.mediaapp.home.domain.models.PopularMusic
import com.mediaapp.home.domain.repository.HomeMediaRepository

class GetPopularMusicUseCase(private val repository: HomeMediaRepository) {
    suspend fun execute(offset: Int, limit: Int): PopularMusic {
        return repository.getPopularMusic(offset, limit)
    }
}