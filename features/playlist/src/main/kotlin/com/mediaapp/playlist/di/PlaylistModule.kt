package com.mediaapp.playlist.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.mediaapp.core.di.FirebaseModule
import com.mediaapp.playlist.data.FirebasePlaylistStorageImpl
import com.mediaapp.playlist.data.PlaylistRepositoryImpl
import com.mediaapp.playlist.data.PlaylistStorage
import com.mediaapp.playlist.domain.repository.PlaylistRepository
import com.mediaapp.playlist.domain.usecase.AddTrackPlaylistUseCase
import com.mediaapp.playlist.domain.usecase.CreatePlaylistUseCase
import com.mediaapp.playlist.domain.usecase.GetPlaylistTracksUseCase
import com.mediaapp.playlist.domain.usecase.GetUserPlaylistsUseCase
import com.mediaapp.playlist.domain.usecase.UpdatePlaylistNameUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [FirebaseModule::class])
class PlaylistModule {

    @Provides
    @Singleton
    fun providesPlaylistStorage(
        databaseReference: DatabaseReference,
        firebaseAuth: FirebaseAuth,
    ): PlaylistStorage = FirebasePlaylistStorageImpl(databaseReference, firebaseAuth)

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

    @Provides
    @Singleton
    fun providesGetUserPlaylistsUseCase(
        playlistRepository: PlaylistRepository,
    ): GetUserPlaylistsUseCase = GetUserPlaylistsUseCase(playlistRepository)

    @Provides
    @Singleton
    fun providesUpdatePlaylistNameUseCase(
        playlistRepository: PlaylistRepository,
    ): UpdatePlaylistNameUseCase = UpdatePlaylistNameUseCase(playlistRepository)

    @Provides
    @Singleton
    fun providesGetPlaylistTracksUseCase(
        playlistRepository: PlaylistRepository,
    ): GetPlaylistTracksUseCase = GetPlaylistTracksUseCase(playlistRepository)

    @Provides
    @Singleton
    fun providesAddTrackPlaylistUseCase(
        playlistRepository: PlaylistRepository,
    ): AddTrackPlaylistUseCase = AddTrackPlaylistUseCase(playlistRepository)
}