package com.mediaapp.home.domain.models

sealed class NetworkRequest {
    sealed class NormalConnect : NetworkRequest() {
        data class NewReleasesRequest(val music: NewMusic) : NormalConnect()
        data class PopularMusicRequest(val music: PopularMusic) : NormalConnect()
        data class TopDownloadsRequest(val music: TopDownloadsMusic) : NormalConnect()
    }

    data class ErrorConnect(val message: String) : NetworkRequest()
}