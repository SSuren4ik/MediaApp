package com.mediaapp.registration.presentation.viewmodel

import android.accounts.AuthenticatorException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.mediaapp.core.utils.ResourceProvider
import com.mediaapp.registration.R
import com.mediaapp.registration.domain.models.Email
import com.mediaapp.registration.domain.models.LoginUserDataModel
import com.mediaapp.registration.domain.models.Password
import com.mediaapp.registration.domain.models.ResponseStatusModel
import com.mediaapp.registration.domain.usecase.LoginUserUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel(
    private val provider: ResourceProvider,
) : ViewModel() {

    @Inject
    lateinit var loginInUserUseCase: LoginUserUseCase

    private val _responseStatus = MutableSharedFlow<ResponseStatusModel>()
    val responseStatus: SharedFlow<ResponseStatusModel> = _responseStatus

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = when (exception) {
            is FirebaseAuthInvalidCredentialsException -> getString(R.string.unknown_user)
            is FirebaseNetworkException -> getString(R.string.invalid_connect)
            is FirebaseTooManyRequestsException -> getString(R.string.too_much_requsets)
            is AuthenticatorException -> getString(R.string.unknown_error)
            else -> getString(R.string.something_is_incorrect)
        }
        emitErrorMessage(message)
    }

    fun loginUser(email: Email, password: Password) {
        viewModelScope.launch(exceptionHandler) {
            if (areFieldsEmpty(email, password)) {
                emitErrorMessage(getString(R.string.field_is_empty))
                return@launch
            }
            if (!isEmailValid(email)) {
                emitErrorMessage(getString(R.string.incorrect_email_format))
                return@launch
            }
            val userDataModel = LoginUserDataModel(email, password)
            loginInUserUseCase.execute(userDataModel)
            _responseStatus.emit(ResponseStatusModel.Success)
        }
    }

    private fun emitErrorMessage(message: String) {
        viewModelScope.launch {
            _responseStatus.emit(ResponseStatusModel.Error(message))
        }
    }

    private fun areFieldsEmpty(email: Email, password: Password): Boolean {
        return email.value.isEmpty() || password.value.isEmpty()
    }

    private fun isEmailValid(email: Email): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
    }

    private fun getString(id: Int): String {
        return provider.getString(id)
    }
}