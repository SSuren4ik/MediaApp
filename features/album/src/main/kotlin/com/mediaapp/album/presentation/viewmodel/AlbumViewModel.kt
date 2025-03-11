package com.mediaapp.album.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediaapp.album.domain.AlbumData
import com.mediaapp.album.domain.GetAlbumTracksUseCase
import com.mediaapp.album.domain.NetworkRequest
import com.mediaapp.core.models.Track
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlbumViewModel : ViewModel() {

    @Inject
    lateinit var getAlbumTracksUseCase: GetAlbumTracksUseCase

    private val _responseStatus = MutableSharedFlow<NetworkRequest>()
    val responseStatus: SharedFlow<NetworkRequest> = _responseStatus

    private lateinit var tracks: List<Track>

    fun getTracks(data: AlbumData) {
        viewModelScope.launch {
            if (::tracks.isInitialized) {
                _responseStatus.emit(NetworkRequest.NormalConnect(tracks))
                return@launch
            }
            val result = getAlbumTracksUseCase.execute(data)
            when (result) {
                is NetworkRequest.ErrorConnect -> _responseStatus.emit(result)
                is NetworkRequest.NormalConnect -> {
                    tracks = result.tracks
                    _responseStatus.emit(result)
                }
            }
        }
    }
}