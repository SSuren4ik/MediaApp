package com.mediaapp.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediaapp.core.utils.ResourceProvider
import com.mediaapp.home.R
import com.mediaapp.home.domain.models.NetworkRequest
import com.mediaapp.home.domain.models.NewMusic
import com.mediaapp.home.domain.models.PopularMusic
import com.mediaapp.home.domain.models.ResponseStatus
import com.mediaapp.home.domain.models.TopDownloadsMusic
import com.mediaapp.home.domain.usecase.GetNewMusicUseCase
import com.mediaapp.home.domain.usecase.GetPopularMusicUseCase
import com.mediaapp.home.domain.usecase.GetTopDownloadsMusicUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel(
    private val resourceProvider: ResourceProvider,
) : ViewModel() {

    @Inject
    lateinit var getPopularMusicUseCase: GetPopularMusicUseCase

    @Inject
    lateinit var getNewMusicUseCase: GetNewMusicUseCase

    @Inject
    lateinit var getTopDownloadsMusicUseCase: GetTopDownloadsMusicUseCase

    private val _responseStatus = MutableSharedFlow<ResponseStatus>()
    val responseStatus: SharedFlow<ResponseStatus> = _responseStatus

    private var error = false
    private var newMusic: NewMusic? = null
    private var topDownloadsMusic: TopDownloadsMusic? = null
    private var popularMusic: PopularMusic? = null

    fun getMusic() {
        viewModelScope.launch {
            if (musicIsNotNull()) {
                val response = ResponseStatus.SuccessResponse(
                    newMusic!!, topDownloadsMusic!!, popularMusic!!
                )
                _responseStatus.emit(response)
                return@launch
            }

            val deferredResults = listOf(async { getPopularMusicUseCase.execute() },
                async { getNewMusicUseCase.execute() },
                async { getTopDownloadsMusicUseCase.execute() })

            val results = deferredResults.awaitAll()

            results.forEach { request ->
                when (request) {
                    is NetworkRequest.ErrorConnect -> error = true
                    is NetworkRequest.NormalConnect -> successRequestHandler(request)
                }
            }

            resultHandler()
        }
    }

    private fun successRequestHandler(request: NetworkRequest.NormalConnect) {
        when (request) {
            is NetworkRequest.NormalConnect.NewReleasesRequest -> newMusic = request.music
            is NetworkRequest.NormalConnect.PopularMusicRequest -> popularMusic = request.music
            is NetworkRequest.NormalConnect.TopDownloadsRequest -> topDownloadsMusic = request.music
        }
    }

    private suspend fun resultHandler() {
        when (error) {
            true -> {
                val errorText = resourceProvider.getString(R.string.network_error_text)
                _responseStatus.emit(ResponseStatus.Error(errorText))
            }

            false -> {
                val response = ResponseStatus.SuccessResponse(
                    newMusic!!, topDownloadsMusic!!, popularMusic!!
                )
                _responseStatus.emit(response)
            }
        }
    }

    private fun musicIsNotNull(): Boolean {
        return newMusic != null && popularMusic != null && topDownloadsMusic != null
    }
}