package com.mediaapp.music_search.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediaapp.core.models.Track
import com.mediaapp.music_search.domain.GetMusicByPrefixUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel : ViewModel() {

    @Inject
    lateinit var getMusicByPrefixUseCase: GetMusicByPrefixUseCase

    private val _responseStatus = MutableSharedFlow<List<Track>>()
    val responseStatus: SharedFlow<List<Track>> = _responseStatus

    fun getMusicByPrefix(prefix: String) {
        viewModelScope.launch {
            if (prefix.isEmpty()) {
                return@launch
            }
            val result = getMusicByPrefixUseCase.execute(prefix)
            _responseStatus.emit(result)
        }
    }
}