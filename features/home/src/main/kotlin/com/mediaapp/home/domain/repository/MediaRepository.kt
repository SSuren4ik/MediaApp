package com.mediaapp.home.domain.repository

import com.mediaapp.home.domain.models.NetworkRequest

interface MediaRepository {
    suspend fun getPopularMusic(): NetworkRequest
    suspend fun getNewMusic(): NetworkRequest
    suspend fun getTopDownloadsMusic(): NetworkRequest
}