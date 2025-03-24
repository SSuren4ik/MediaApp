package com.mediaapp.registration.data

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.mediaapp.registration.domain.models.LoginUserDataModel
import com.mediaapp.registration.domain.models.SignUpUserDataModel
import kotlinx.coroutines.tasks.await

class FirebaseAuthStorage(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference,
) : AuthStorage {

    override suspend fun loginUser(userDataModel: LoginUserDataModel) {
        firebaseAuth.signInWithEmailAndPassword(
            userDataModel.email.value, userDataModel.password.value
        ).await()
    }

    override suspend fun saveUserInStorage(uid: String, userDataModel: SignUpUserDataModel) {
        val userInfo =
            mapOf("email" to userDataModel.email.value, "username" to userDataModel.userName.value)
        firebaseDatabase.child("Users").child(uid).setValue(userInfo).await()
    }

    override suspend fun registerUser(userDataModel: SignUpUserDataModel): AuthResult {
        val result = firebaseAuth.createUserWithEmailAndPassword(
            userDataModel.email.value, userDataModel.password.value
        ).await()
        val uid = result.user?.uid
        if (uid != null) {
            saveUserInStorage(uid, userDataModel)
        }
        return result
    }
}