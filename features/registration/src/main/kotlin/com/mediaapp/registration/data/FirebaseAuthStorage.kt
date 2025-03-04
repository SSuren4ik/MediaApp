package com.mediaapp.registration.data

import com.google.firebase.auth.FirebaseAuth
import com.mediaapp.registration.domain.models.LoginUserDataModel
import com.mediaapp.registration.domain.models.SignUpUserDataModel
import kotlinx.coroutines.tasks.await

class FirebaseAuthStorage(
    private val firebaseAuth: FirebaseAuth,
) : AuthStorage {

    override suspend fun loginUser(userDataModel: LoginUserDataModel) {
        firebaseAuth.signInWithEmailAndPassword(
            userDataModel.email.value, userDataModel.password.value
        ).await()
    }

    override suspend fun registerUser(userDataModel: SignUpUserDataModel) {
        firebaseAuth.createUserWithEmailAndPassword(
            userDataModel.email.value, userDataModel.password.value
        ).await()
    }
}