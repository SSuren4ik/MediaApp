package com.mediaapp.home.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediaapp.core.models.NetworkException
import com.mediaapp.core.utils.ResourceProvider
import com.mediaapp.home.R
import com.mediaapp.home.domain.models.NewMusic
import com.mediaapp.home.domain.models.PopularMusic
import com.mediaapp.home.domain.models.ResponseStatus
import com.mediaapp.home.domain.models.TopDownloadsMusic
import com.mediaapp.home.domain.usecase.GetNewMusicUseCase
import com.mediaapp.home.domain.usecase.GetPopularMusicUseCase
import com.mediaapp.home.domain.usecase.GetTopDownloadsMusicUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
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

    private var newMusic = NewMusic()
    private var topDownloadsMusic = TopDownloadsMusic()
    private var popularMusic = PopularMusic()

    private var popularMusicOffset = 0
    private var newMusicOffset = 0
    private var topDownloadsMusicOffset = 0
    private val limit = 10

    private var loadMorePopularMusicJob: Job? = null
    private var loadMoreNewMusicJob: Job? = null
    private var loadMoreTopDownloadsMusicJob: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val errorText = when (exception) {
            is NetworkException -> {
                Log.d("HomeViewModel", "NetworkException: ${exception.message}")
                resourceProvider.getString(R.string.network_error_text)
            }

            is HttpException -> {
                Log.d("HomeViewModel", "HttpException: ${exception.message}")
                resourceProvider.getString(R.string.http_error_text)
            }
            else -> resourceProvider.getString(R.string.unknown_error_text)
        }
        viewModelScope.launch {
            _responseStatus.emit(ResponseStatus.Error(errorText))
        }
    }

    fun getMusic() {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            if (musicIsNotEmpty()) {
                _responseStatus.emit(
                    ResponseStatus.SuccessResponse(
                        newMusic, topDownloadsMusic, popularMusic
                    )
                )
                return@launch
            }

            val deferredResults = listOf(async { getPopularMusicUseCase.execute(popularMusicOffset, limit) },
                    async { getNewMusicUseCase.execute(newMusicOffset, limit) },
                    async { getTopDownloadsMusicUseCase.execute(topDownloadsMusicOffset, limit) })

            val results = deferredResults.awaitAll()
            results.forEach { request -> successRequestHandler(request) }

            resultHandler()
        }
    }

    fun loadMorePopularMusic() {
        if (loadMorePopularMusicJob?.isActive == true) return

        loadMorePopularMusicJob = viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            val result = getPopularMusicUseCase.execute(popularMusicOffset + limit, limit)
            popularMusicOffset += limit
            popularMusic = PopularMusic(popularMusic.value + result.value)
            resultHandler()
        }
    }

    fun loadMoreNewMusic() {
        if (loadMoreNewMusicJob?.isActive == true) return

        loadMoreNewMusicJob = viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            val result = getNewMusicUseCase.execute(newMusicOffset + limit, limit)
            newMusicOffset += limit
            newMusic = NewMusic(newMusic.value + result.value)
            resultHandler()
        }
    }

    fun loadMoreTopDownloadsMusic() {
        if (loadMoreTopDownloadsMusicJob?.isActive == true) return

        loadMoreTopDownloadsMusicJob = viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            val result = getTopDownloadsMusicUseCase.execute(topDownloadsMusicOffset + limit, limit)
            topDownloadsMusicOffset += limit
            topDownloadsMusic = TopDownloadsMusic(topDownloadsMusic.value + result.value)
            resultHandler()
        }
    }

    private fun successRequestHandler(request: Any) {
        when (request) {
            is NewMusic -> newMusic = request
            is PopularMusic -> popularMusic = request
            is TopDownloadsMusic -> topDownloadsMusic = request
        }
    }

    private suspend fun resultHandler() {
        if (musicIsNotEmpty()) {
            _responseStatus.emit(
                ResponseStatus.SuccessResponse(
                    newMusic, topDownloadsMusic, popularMusic
                )
            )
        }
    }

    private fun musicIsNotEmpty(): Boolean {
        return newMusic.value.isNotEmpty() && popularMusic.value.isNotEmpty() && topDownloadsMusic.value.isNotEmpty()
    }
}