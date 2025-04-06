package com.mediaapp.registration.domain.usecase

import com.mediaapp.registration.domain.models.SignUpUserDataModel
import com.mediaapp.registration.domain.repository.UserRegistrationRepository

class SignUpUserUseCase(private val repository: UserRegistrationRepository) {
    suspend fun execute(userDataModel: SignUpUserDataModel) {
        repository.registerUser(userDataModel)
    }
}