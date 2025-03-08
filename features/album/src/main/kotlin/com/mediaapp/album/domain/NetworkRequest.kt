package com.mediaapp.album.domain

import com.mediaapp.core.models.Track

sealed class NetworkRequest {
    data class NormalConnect(val tracks: List<Track>) : NetworkRequest()
    data class ErrorConnect(val message: String) : NetworkRequest()
}