package com.mediaapp.registration.domain.models

sealed class ResponseStatusModel {
    data object Success : ResponseStatusModel()
    data class Error(val message: String) : ResponseStatusModel()
}