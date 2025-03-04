package com.mediaapp.registration.di

import com.google.firebase.auth.FirebaseAuth
import com.mediaapp.registration.data.AuthStorage
import com.mediaapp.registration.data.FirebaseAuthStorage
import com.mediaapp.registration.data.UserRegistrationRepositoryImpl
import com.mediaapp.registration.domain.repository.UserRegistrationRepository
import com.mediaapp.registration.domain.usecase.LoginUserUseCase
import com.mediaapp.registration.domain.usecase.SignUpUserUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RegistrationModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthInstance(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesRegistrationStorage(
        firebaseAuth: FirebaseAuth,
    ): AuthStorage = FirebaseAuthStorage(firebaseAuth)

    @Provides
    @Singleton
    fun providesUserRepositoryImpl(storage: AuthStorage): UserRegistrationRepository =
        UserRegistrationRepositoryImpl(storage)

    @Provides
    @Singleton
    fun provideLoginUserUseCase(repository: UserRegistrationRepository) = LoginUserUseCase(repository)

    @Provides
    @Singleton
    fun provideSignUpUserUseCase(repository: UserRegistrationRepository) =
        SignUpUserUseCase(repository)
}