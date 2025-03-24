package com.mediaapp.playlist.presentation.user_playlists.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediaapp.core.models.PlaylistData
import com.mediaapp.playlist.domain.usecase.CreatePlaylistUseCase
import com.mediaapp.playlist.domain.usecase.GetUserPlaylistsUseCase
import com.mediaapp.playlist.domain.usecase.UpdatePlaylistNameUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserPlaylistsViewModel : ViewModel() {

    @Inject
    lateinit var createPlaylistUseCase: CreatePlaylistUseCase

    @Inject
    lateinit var getUserPlaylistsUseCase: GetUserPlaylistsUseCase

    private val _responseStatus = MutableSharedFlow<List<PlaylistData>>()
    val responseStatus: SharedFlow<List<PlaylistData>> = _responseStatus

    private val _createStatus = MutableSharedFlow<Unit>()
    val createStatus: SharedFlow<Unit> = _createStatus

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = when (exception) {
            is IllegalStateException -> {
                Log.d("PlaylistViewModel", "IllegalStateException ${exception.message}")
            }

            else -> {
                Log.d("PlaylistViewModel", "Exception ${exception.message}")
            }
        }
    }

    fun createPlaylist(playlistName: String) {
        viewModelScope.launch(exceptionHandler) {
            createPlaylistUseCase.execute(playlistName)
            _createStatus.emit(Unit)
        }
    }

    fun getUserPlaylists() {
        viewModelScope.launch(exceptionHandler) {
            val result = getUserPlaylistsUseCase.execute()
            _responseStatus.emit(result)
        }
    }
}