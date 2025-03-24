package com.mediaapp.playlist.presentation.user_playlists.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediaapp.core.models.PlaylistData
import com.mediaapp.playlist.domain.usecase.GetPlaylistTracksUseCase
import com.mediaapp.playlist.domain.usecase.UpdatePlaylistNameUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistViewModel : ViewModel() {

    @Inject
    lateinit var getPlaylistTracks: GetPlaylistTracksUseCase

    @Inject
    lateinit var updatePlaylistNameUseCase: UpdatePlaylistNameUseCase

    private val _getTracksResponseStatus = MutableSharedFlow<PlaylistData>()
    val getTracksResponseStatus: SharedFlow<PlaylistData> = _getTracksResponseStatus

    private val _updateNameResponseStatus = MutableSharedFlow<Unit>()
    val updateNameResponseStatus: SharedFlow<Unit> = _updateNameResponseStatus

    private var playlistName = ""

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

    fun getPlaylistTracks(playlistId: String) {
        viewModelScope.launch(exceptionHandler) {
            val result = getPlaylistTracks.execute(playlistId)
            _getTracksResponseStatus.emit(result)
        }
    }

    fun getPlaylistName(): String {
        return playlistName
    }

    fun updatePlaylistName(playlistNewName: String, playlistOldName: String) {
        viewModelScope.launch(exceptionHandler) {
            updatePlaylistNameUseCase.execute(playlistNewName, playlistOldName)
            playlistName = playlistNewName
            _updateNameResponseStatus.emit(Unit)
        }
    }

}