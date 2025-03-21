package com.mediaapp.home.domain.repository

import com.mediaapp.home.domain.models.NewMusic
import com.mediaapp.home.domain.models.PopularMusic
import com.mediaapp.home.domain.models.TopDownloadsMusic

interface HomeMediaRepository {
    suspend fun getPopularMusic(offset: Int, limit: Int): PopularMusic
    suspend fun getNewMusic(offset: Int, limit: Int): NewMusic
    suspend fun getTopDownloadsMusic(offset: Int, limit: Int): TopDownloadsMusic
}