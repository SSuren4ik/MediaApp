package com.mediaapp.playlist.presentation.selected_user_playlists.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mediaapp.core.utils.ResourceProvider

class SelectedUserPlaylistsViewModelFactory(
    private val resourceProvider: ResourceProvider,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelectedUserPlaylistsViewModel::class.java)) {
            return SelectedUserPlaylistsViewModel(resourceProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}