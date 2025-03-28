package com.mediaapp.playlist.presentation.playlist_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mediaapp.core.utils.ResourceProvider

class CurrentPlaylistViewModelFactory(
    private val resourceProvider: ResourceProvider,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentPlaylistViewModel::class.java)) {
            return CurrentPlaylistViewModel(resourceProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}