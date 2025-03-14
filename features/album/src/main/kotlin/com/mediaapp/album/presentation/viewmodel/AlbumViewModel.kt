package com.mediaapp.album.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediaapp.album.domain.AlbumData
import com.mediaapp.album.domain.GetAlbumTracksUseCase
import com.mediaapp.album.domain.NetworkResponse
import com.mediaapp.core.models.Track
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlbumViewModel : ViewModel() {

    @Inject
    lateinit var getAlbumTracksUseCase: GetAlbumTracksUseCase

    private val _responseStatus = MutableSharedFlow<NetworkResponse>()
    val responseStatus: SharedFlow<NetworkResponse> = _responseStatus

    private lateinit var tracks: List<Track>

    fun getTracks(data: AlbumData) {
        viewModelScope.launch {
            if (::tracks.isInitialized) {
                _responseStatus.emit(NetworkResponse.Success(tracks))
                return@launch
            }
            val result = getAlbumTracksUseCase.execute(data)
            when (result) {
                is NetworkResponse.ErrorConnect -> _responseStatus.emit(result)
                is NetworkResponse.Success -> {
                    tracks = result.tracks
                    _responseStatus.emit(result)
                }
            }
        }
    }
}