package com.mediaapp.music_search.data

import com.mediaapp.core.api.MediaServiceApi
import com.mediaapp.core.models.Track
import com.mediaapp.music_search.domain.SearchRepository

class SearchRepositoryImpl(
    private val service: MediaServiceApi,
    private val apiKey: String,
) : SearchRepository {
    override suspend fun searchMusicByPrefix(prefix: String): List<Track> {
        try {
            val musicData = service.getTracksByPrefix(apiKey, prefix)
            return musicData.results
        } catch (e: Exception) {
            throw e
        }
    }
}