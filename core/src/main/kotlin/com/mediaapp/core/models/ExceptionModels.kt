package com.mediaapp.core.models

class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)
class UnknownException(message: String, cause: Throwable? = null) : Exception(message, cause)

sealed class FirebaseExceptions(val error: String) : Exception(error) {

    data class UserNotAuthenticatedException(val errorMessage: String) : FirebaseExceptions(errorMessage)

    data class PlaylistAlreadyExistsException(val errorMessage: String) : FirebaseExceptions(errorMessage)

    data class PlaylistSavedAlreadyException(val errorMessage: String) : FirebaseExceptions(errorMessage)

    data class PlaylistNotFoundException(val errorMessage: String) : FirebaseExceptions(errorMessage)

    data class TrackAlreadyExistsException(val errorMessage: String) : FirebaseExceptions(errorMessage)

    data class TrackNotFoundException(val errorMessage: String) : FirebaseExceptions(errorMessage)
}