package com.mediaapp.registration.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.mediaapp.core.utils.ResourceProvider
import com.mediaapp.registration.R
import com.mediaapp.registration.domain.models.Email
import com.mediaapp.registration.domain.models.Password
import com.mediaapp.registration.domain.models.ResponseStatusModel
import com.mediaapp.registration.domain.models.SignUpUserDataModel
import com.mediaapp.registration.domain.models.UserName
import com.mediaapp.registration.domain.usecase.SignUpUserUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignUpViewModel(
    private val provider: ResourceProvider,
) : ViewModel() {

    @Inject
    lateinit var signUpUserUseCase: SignUpUserUseCase

    private val _responseStatus = MutableSharedFlow<ResponseStatusModel>()
    val responseStatus: SharedFlow<ResponseStatusModel> = _responseStatus

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = when (exception) {
            is FirebaseAuthWeakPasswordException -> getString(R.string.bad_password)
            is FirebaseAuthInvalidCredentialsException -> getString(R.string.incorrect_email_input)
            is FirebaseAuthUserCollisionException -> getString(R.string.account_already_exist)
            else -> getString(R.string.something_is_incorrect)
        }
        emitErrorMessage(message)
    }

    fun registerUser(email: Email, username: UserName, password: Password) {
        viewModelScope.launch(exceptionHandler) {
            if (areFieldsEmpty(email, username, password)) {
                emitErrorMessage(getString(R.string.field_is_empty))
                return@launch
            }
            if (!isEmailValid(email)) {
                emitErrorMessage(getString(R.string.incorrect_email_format))
                return@launch
            }
            val userDataModel = SignUpUserDataModel(email, password, username)
            signUpUserUseCase.execute(userDataModel)
            _responseStatus.emit(ResponseStatusModel.Success)
        }
    }

    private fun emitErrorMessage(message: String) {
        viewModelScope.launch {
            _responseStatus.emit(ResponseStatusModel.Error(message))
        }
    }

    private fun areFieldsEmpty(email: Email, username: UserName, password: Password): Boolean {
        return email.value.isEmpty() || password.value.isEmpty() || username.value.isEmpty()
    }

    private fun isEmailValid(email: Email): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
    }

    private fun getString(id: Int): String {
        return provider.getString(id)
    }
}