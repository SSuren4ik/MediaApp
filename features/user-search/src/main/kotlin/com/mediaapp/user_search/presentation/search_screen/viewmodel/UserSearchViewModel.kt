package com.mediaapp.user_search.presentation.search_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediaapp.user_search.domain.FindUserByPrefixUseCase
import com.mediaapp.user_search.domain.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserSearchViewModel : ViewModel() {

    @Inject
    lateinit var findUserByPrefixUseCase: FindUserByPrefixUseCase

    private val _response = MutableSharedFlow<List<UserData>>()
    val response: SharedFlow<List<UserData>> = _response


    fun findUserByPrefix(prefix: String) {
        if (prefix.isEmpty())
            return
        viewModelScope.launch(Dispatchers.IO) {
            val result = findUserByPrefixUseCase.execute(prefix)
            _response.emit(result)
        }
    }
}