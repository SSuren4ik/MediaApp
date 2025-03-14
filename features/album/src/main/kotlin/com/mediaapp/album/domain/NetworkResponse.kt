package com.mediaapp.album.domain

import com.mediaapp.core.models.Track

sealed class NetworkResponse {
    data class Success(val tracks: List<Track>) : NetworkResponse()
    data class ErrorConnect(val message: String) : NetworkResponse()
}