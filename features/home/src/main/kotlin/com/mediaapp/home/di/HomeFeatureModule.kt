package com.mediaapp.home.di

import com.mediaapp.core.di.MediaModule
import com.mediaapp.core.di.NetworkModule
import com.mediaapp.core.domain.MediaService
import com.mediaapp.home.data.HomeMediaRepositoryImpl
import com.mediaapp.home.domain.repository.HomeMediaRepository
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
    ): HomeMediaRepository {
        return HomeMediaRepositoryImpl(service, apiKey)
    }

    @Provides
    fun provideGetPopularMusicUseCase(repository: HomeMediaRepository): GetPopularMusicUseCase {
        return GetPopularMusicUseCase(repository)
    }

    @Provides
    fun provideGetNewMusicUseCase(repository: HomeMediaRepository): GetNewMusicUseCase {
        return GetNewMusicUseCase(repository)
    }

    @Provides
    fun provideGetTopDownloadsMusicUseCase(repository: HomeMediaRepository): GetTopDownloadsMusicUseCase {
        return GetTopDownloadsMusicUseCase(repository)
    }
}