package com.mediaapp.registration.data

import com.mediaapp.registration.domain.models.LoginUserDataModel
import com.mediaapp.registration.domain.models.SignUpUserDataModel
import com.mediaapp.registration.domain.repository.UserRegistrationRepository

class UserRegistrationRepositoryImpl(
    private var firebaseAuthService: AuthStorage,
) : UserRegistrationRepository {

    override suspend fun registerUser(userDataModel: SignUpUserDataModel) {
        firebaseAuthService.registerUser(userDataModel)
    }

    override suspend fun loginUser(userDataModel: LoginUserDataModel) {
        firebaseAuthService.loginUser(userDataModel)
    }
}