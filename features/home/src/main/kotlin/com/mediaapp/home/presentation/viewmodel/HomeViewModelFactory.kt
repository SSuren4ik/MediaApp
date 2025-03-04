package com.mediaapp.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mediaapp.core.utils.ResourceProvider

class HomeViewModelFactory(
    private val resourceProvider: ResourceProvider,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(resourceProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}