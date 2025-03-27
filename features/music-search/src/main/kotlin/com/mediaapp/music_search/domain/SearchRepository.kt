package com.mediaapp.music_search.domain

import com.mediaapp.core.models.Track

interface SearchRepository {
    suspend fun searchMusicByPrefix(prefix: String): List<Track>
}