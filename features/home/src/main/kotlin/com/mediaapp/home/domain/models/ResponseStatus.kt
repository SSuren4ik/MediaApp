package com.mediaapp.home.domain.models

sealed class ResponseStatus {
    data class SuccessResponse(
        val newMusic: NewMusic,
        val topDownloadsMusic: TopDownloadsMusic,
        val popularMusic: PopularMusic,
    ) : ResponseStatus()

    data class Error(val error: String) : ResponseStatus()
}