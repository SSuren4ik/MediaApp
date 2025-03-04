package com.mediaapp.registration.domain.repository

import com.mediaapp.registration.domain.models.LoginUserDataModel
import com.mediaapp.registration.domain.models.SignUpUserDataModel

interface UserRegistrationRepository {
    suspend fun registerUser(userDataModel: SignUpUserDataModel)
    suspend fun loginUser(userDataModel: LoginUserDataModel)
}