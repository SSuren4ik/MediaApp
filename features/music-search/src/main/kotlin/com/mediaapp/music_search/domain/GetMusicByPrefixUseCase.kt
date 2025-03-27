package com.mediaapp.music_search.domain

import com.mediaapp.core.models.Track

class GetMusicByPrefixUseCase(
    private val repository: SearchRepository,
) {
    suspend fun execute(prefix: String): List<Track> {
        return repository.searchMusicByPrefix(prefix)
    }
}