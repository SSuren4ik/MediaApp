package com.mediaapp.home.domain.usecase

import com.mediaapp.home.domain.models.NewMusic
import com.mediaapp.home.domain.repository.HomeMediaRepository

class GetNewMusicUseCase(private val repository: HomeMediaRepository) {
    suspend fun execute(offset: Int, limit: Int): NewMusic {
        return repository.getNewMusic(offset, limit)
    }
}