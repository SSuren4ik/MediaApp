package com.mediaapp.playlist.presentation.user_playlists.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediaapp.core.models.FirebaseExceptions
import com.mediaapp.core.utils.ResourceProvider
import com.mediaapp.playlist.R
import com.mediaapp.playlist.domain.models.CurrentPlaylistResponseStatusModel
import com.mediaapp.playlist.domain.usecase.GetPlaylistTracksUseCase
import com.mediaapp.playlist.domain.usecase.UpdatePlaylistNameUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrentPlaylistViewModel(
    private val provider: ResourceProvider,
) : ViewModel() {

    @Inject
    lateinit var getPlaylistTracks: GetPlaylistTracksUseCase

    @Inject
    lateinit var updatePlaylistNameUseCase: UpdatePlaylistNameUseCase

    private val _responseStatus = MutableSharedFlow<CurrentPlaylistResponseStatusModel>()
    val responseStatus: SharedFlow<CurrentPlaylistResponseStatusModel> = _responseStatus

    private var playlistName = ""

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = when (exception) {
            is FirebaseExceptions.UserNotAuthenticatedException -> getString(R.string.user_not_authenticated_message)
            is FirebaseExceptions.PlaylistAlreadyExistsException -> getString(R.string.playlist_already_exists_message)
            is FirebaseExceptions.PlaylistNotFoundException -> getString(R.string.playlist_not_found_message)
            is FirebaseExceptions.TrackAlreadyExistsException -> getString(R.string.track_already_exists_message)
            else -> "Неизвестная ошибка: ${exception.message}"
        }
        emitErrorMessage(message)
    }

    fun getPlaylistTracks(playlistId: String) {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            val result = getPlaylistTracks.execute(playlistId)
            _responseStatus.emit(result)
        }
    }

    fun updatePlaylistName(playlistNewName: String, playlistOldName: String) {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            updatePlaylistNameUseCase.execute(playlistNewName, playlistOldName)
            playlistName = playlistNewName
            _responseStatus.emit(CurrentPlaylistResponseStatusModel.Success.SuccessUpdatePlaylistName)
        }
    }

    fun getPlaylistName(): String {
        return playlistName
    }

    private fun emitErrorMessage(message: String) {
        viewModelScope.launch {
            _responseStatus.emit(CurrentPlaylistResponseStatusModel.Error(message))
        }
    }

    private fun getString(resId: Int): String {
        return provider.getString(resId)
    }
}