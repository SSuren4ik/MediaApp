package com.mediaapp.registration.data

import com.google.firebase.auth.AuthResult
import com.mediaapp.registration.domain.models.LoginUserDataModel
import com.mediaapp.registration.domain.models.SignUpUserDataModel

interface AuthStorage {

    suspend fun loginUser(userDataModel: LoginUserDataModel)

    suspend fun registerUser(userDataModel: SignUpUserDataModel): AuthResult

    suspend fun saveUserInStorage(uid: String, userDataModel: SignUpUserDataModel)
}