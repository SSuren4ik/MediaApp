package com.mediaapp.registration.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.mediaapp.core.di.FirebaseModule
import com.mediaapp.registration.data.AuthStorage
import com.mediaapp.registration.data.FirebaseAuthStorage
import com.mediaapp.registration.data.UserRegistrationRepositoryImpl
import com.mediaapp.registration.domain.repository.UserRegistrationRepository
import com.mediaapp.registration.domain.usecase.LoginUserUseCase
import com.mediaapp.registration.domain.usecase.SignUpUserUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [FirebaseModule::class])
class RegistrationModule {
    @Provides
    @Singleton
    fun providesRegistrationStorage(
        firebaseAuth: FirebaseAuth,
        databaseReference: DatabaseReference,
    ): AuthStorage = FirebaseAuthStorage(firebaseAuth, databaseReference)

    @Provides
    @Singleton
    fun providesUserRepositoryImpl(storage: AuthStorage): UserRegistrationRepository =
        UserRegistrationRepositoryImpl(storage)

    @Provides
    @Singleton
    fun provideLoginUserUseCase(repository: UserRegistrationRepository) =
        LoginUserUseCase(repository)

    @Provides
    @Singleton
    fun provideSignUpUserUseCase(repository: UserRegistrationRepository) =
        SignUpUserUseCase(repository)
}