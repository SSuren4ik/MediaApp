package com.mediaapp.core.models

class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)
class UnknownException(message: String, cause: Throwable? = null) : Exception(message, cause)
