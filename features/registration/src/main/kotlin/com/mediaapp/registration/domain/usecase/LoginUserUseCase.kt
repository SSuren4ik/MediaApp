package com.mediaapp.registration.domain.usecase

import com.mediaapp.registration.domain.models.LoginUserDataModel
import com.mediaapp.registration.domain.repository.UserRegistrationRepository

class LoginUserUseCase(private val repository: UserRegistrationRepository) {

    suspend fun execute(dataModel: LoginUserDataModel) {
        repository.loginUser(dataModel)
    }
}