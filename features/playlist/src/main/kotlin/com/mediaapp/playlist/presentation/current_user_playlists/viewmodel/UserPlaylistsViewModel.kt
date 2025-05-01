package com.mediaapp.playlist.presentation.current_user_playlists.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediaapp.core.models.FirebaseExceptions
import com.mediaapp.core.models.Track
import com.mediaapp.core.utils.ResourceProvider
import com.mediaapp.playlist.R
import com.mediaapp.playlist.domain.models.UserPlaylistsResponseStatusModel
import com.mediaapp.playlist.domain.usecase.AddTrackPlaylistUseCase
import com.mediaapp.playlist.domain.usecase.CreatePlaylistUseCase
import com.mediaapp.playlist.domain.usecase.GetUserPlaylistsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserPlaylistsViewModel(
    private val provider: ResourceProvider,
) : ViewModel() {

    @Inject
    lateinit var createPlaylistUseCase: CreatePlaylistUseCase

    @Inject
    lateinit var getUserPlaylistsUseCase: GetUserPlaylistsUseCase

    @Inject
    lateinit var addTrackPlaylistUseCase: AddTrackPlaylistUseCase

    private val _responseStatus = MutableSharedFlow<UserPlaylistsResponseStatusModel>()
    val responseStatus: SharedFlow<UserPlaylistsResponseStatusModel> = _responseStatus

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = when (exception) {
            is FirebaseExceptions.UserNotAuthenticatedException -> getString(R.string.user_not_authenticated_message)
            is FirebaseExceptions.PlaylistAlreadyExistsException -> getString(R.string.playlist_already_exists_message)
            is FirebaseExceptions.PlaylistNotFoundException -> getString(R.string.playlist_not_found_message)
            is FirebaseExceptions.PlaylistSavedAlreadyException -> getString(R.string.playlist_saved_already_message)
            is FirebaseExceptions.TrackAlreadyExistsException -> getString(R.string.track_already_exists_message)
            else -> {
                "Неизвестная ошибка: ${exception.message}"
            }
        }
        emitErrorMessage(message)
    }

    private fun launch(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(exceptionHandler + Dispatchers.IO, block = block)

    fun createPlaylist(playlistName: String) {
        launch {
            createPlaylistUseCase.execute(playlistName)
            _responseStatus.emit(UserPlaylistsResponseStatusModel.Success.SuccessCreatePlaylist)
        }
    }

    fun getUserPlaylists() {
        launch {
            val result = getUserPlaylistsUseCase.execute()
            _responseStatus.emit(result)
        }
    }

    fun addTrackToPlaylist(playlistId: String, track: Track) {
        launch {
            addTrackPlaylistUseCase.execute(playlistId, track)
            _responseStatus.emit(UserPlaylistsResponseStatusModel.Success.SuccessAddSongToPlaylist)
        }
    }

    private fun emitErrorMessage(message: String) {
        viewModelScope.launch {
            _responseStatus.emit(UserPlaylistsResponseStatusModel.Error(message))
        }
    }

    private fun getString(resId: Int): String {
        return provider.getString(resId)
    }
}