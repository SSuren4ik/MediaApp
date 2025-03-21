package com.mediaapp.playlist.di

import com.google.firebase.database.DatabaseReference
import com.mediaapp.core.di.FirebaseModule
import com.mediaapp.playlist.data.FirebasePlaylistStorageImpl
import com.mediaapp.playlist.data.PlaylistRepositoryImpl
import com.mediaapp.playlist.data.PlaylistStorage
import com.mediaapp.playlist.domain.repository.PlaylistRepository
import com.mediaapp.playlist.domain.usecase.CreatePlaylistUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [FirebaseModule::class])
class PlaylistModule {

    @Provides
    @Singleton
    fun providesPlaylistStorage(
        databaseReference: DatabaseReference,
    ): PlaylistStorage = FirebasePlaylistStorageImpl(databaseReference)

    @Provides
    @Singleton
    fun providesPlaylistRepository(
        playlistStorage: PlaylistStorage,
    ): PlaylistRepository = PlaylistRepositoryImpl(playlistStorage)

    @Provides
    @Singleton
    fun providesCreatePlaylistUseCase(
        playlistRepository: PlaylistRepository,
    ): CreatePlaylistUseCase = CreatePlaylistUseCase(playlistRepository)
}