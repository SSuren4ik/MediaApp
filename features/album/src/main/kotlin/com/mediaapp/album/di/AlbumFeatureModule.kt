package com.mediaapp.album.di

import com.mediaapp.album.data.AlbumMediaRepositoryImpl
import com.mediaapp.album.domain.AlbumMediaRepository
import com.mediaapp.album.domain.GetAlbumTracksUseCase
import com.mediaapp.core.api.MediaServiceApi
import com.mediaapp.core.di.MediaModule
import com.mediaapp.core.di.NetworkModule
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [NetworkModule::class, MediaModule::class])
class AlbumFeatureModule {

    @Provides
    fun provideMediaRepository(
        service: MediaServiceApi,
        @Named("apiKey") apiKey: String,
    ): AlbumMediaRepository {
        return AlbumMediaRepositoryImpl(service, apiKey)
    }

    @Provides
    fun provideGetPopularMusicUseCase(repository: AlbumMediaRepository): GetAlbumTracksUseCase {
        return GetAlbumTracksUseCase(repository)
    }
}