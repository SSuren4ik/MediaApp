package com.mediaapp.registration.domain.models

data class SignUpUserDataModel(
    val email: Email,
    val password: Password,
    val userName: UserName,
)