package com.mediaapp.home.di

import com.mediaapp.core.di.MediaModule
import com.mediaapp.core.di.NetworkModule
import com.mediaapp.core.domain.MediaService
import com.mediaapp.home.data.MediaRepositoryImpl
import com.mediaapp.home.domain.repository.MediaRepository
import com.mediaapp.home.domain.usecase.GetNewMusicUseCase
import com.mediaapp.home.domain.usecase.GetPopularMusicUseCase
import com.mediaapp.home.domain.usecase.GetTopDownloadsMusicUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [NetworkModule::class, MediaModule::class])
class HomeFeatureModule {

    @Provides
    fun provideMediaRepository(
        service: MediaService,
        @Named("apiKey") apiKey: String,
    ): MediaRepository {
        return MediaRepositoryImpl(service, apiKey)
    }

    @Provides
    fun provideGetPopularMusicUseCase(repository: MediaRepository): GetPopularMusicUseCase {
        return GetPopularMusicUseCase(repository)
    }

    @Provides
    fun provideGetNewMusicUseCase(repository: MediaRepository): GetNewMusicUseCase {
        return GetNewMusicUseCase(repository)
    }

    @Provides
    fun provideGetTopDownloadsMusicUseCase(repository: MediaRepository): GetTopDownloadsMusicUseCase {
        return GetTopDownloadsMusicUseCase(repository)
    }
}