package com.mediaapp.user_search.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.mediaapp.core.di.FirebaseModule
import com.mediaapp.user_search.data.UserSearchRepositoryFirebaseImpl
import com.mediaapp.user_search.data.UserSearchStorage
import com.mediaapp.user_search.data.UserSearchStorageFirebaseImpl
import com.mediaapp.user_search.domain.FindUserByPrefixUseCase
import com.mediaapp.user_search.domain.UserSearchRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [FirebaseModule::class])
class UserSearchModule {

    @Provides
    @Singleton
    fun provideUserSearchStorage(
        firebaseAuth: FirebaseAuth,
        database: DatabaseReference,
    ): UserSearchStorage {
        return UserSearchStorageFirebaseImpl(database, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideUserSearchRepository(storage: UserSearchStorage): UserSearchRepository {
        return UserSearchRepositoryFirebaseImpl(storage)
    }

    @Provides
    @Singleton
    fun provideUserSearchUseCase(repository: UserSearchRepository): FindUserByPrefixUseCase {
        return FindUserByPrefixUseCase(repository)
    }
}