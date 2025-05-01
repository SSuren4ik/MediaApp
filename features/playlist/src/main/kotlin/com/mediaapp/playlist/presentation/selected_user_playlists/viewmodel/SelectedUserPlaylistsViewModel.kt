package com.mediaapp.playlist.presentation.selected_user_playlists.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediaapp.core.models.FirebaseExceptions
import com.mediaapp.core.models.PlaylistData
import com.mediaapp.core.utils.ResourceProvider
import com.mediaapp.playlist.R
import com.mediaapp.playlist.domain.models.UserPlaylistsResponseStatusModel
import com.mediaapp.playlist.domain.usecase.GetSelectedUserPlaylistsUseCase
import com.mediaapp.playlist.domain.usecase.SaveSelectedPlaylistUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectedUserPlaylistsViewModel(
    private val provider: ResourceProvider,
) : ViewModel() {

    @Inject
    lateinit var getSelectedUserPlaylistsUseCase: GetSelectedUserPlaylistsUseCase

    @Inject
    lateinit var saveSelectedPlaylistUseCase: SaveSelectedPlaylistUseCase

    private val _responseStatus = MutableSharedFlow<UserPlaylistsResponseStatusModel>()
    val responseStatus: SharedFlow<UserPlaylistsResponseStatusModel> = _responseStatus

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = when (exception) {
            is FirebaseExceptions.UserNotAuthenticatedException -> getString(R.string.user_not_authenticated_message)
            is FirebaseExceptions.PlaylistAlreadyExistsException -> getString(R.string.playlist_already_exists_message)
            is FirebaseExceptions.PlaylistSavedAlreadyException -> getString(R.string.playlist_saved_already_message)
            is FirebaseExceptions.PlaylistNotFoundException -> getString(R.string.playlist_not_found_message)
            is FirebaseExceptions.TrackAlreadyExistsException -> getString(R.string.track_already_exists_message)
            else -> "Неизвестная ошибка: ${exception.message}"
        }
        emitErrorMessage(message)
    }

    private fun launch(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(exceptionHandler + Dispatchers.IO, block = block)

    fun getUserPlaylists(userId: String) {
        launch {
            val result = getSelectedUserPlaylistsUseCase.execute(userId)
            _responseStatus.emit(result)
        }
    }

    fun saveSelectedPlaylist(playlistData: PlaylistData) {
        launch {
            saveSelectedPlaylistUseCase.execute(playlistData)
            _responseStatus.emit(UserPlaylistsResponseStatusModel.Success.SuccessSaveSelectedPlaylist)
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