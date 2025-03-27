package com.mediaapp.music_search.di

import com.mediaapp.core.api.MediaServiceApi
import com.mediaapp.core.di.MediaModule
import com.mediaapp.core.di.NetworkModule
import com.mediaapp.music_search.data.SearchRepositoryImpl
import com.mediaapp.music_search.domain.GetMusicByPrefixUseCase
import com.mediaapp.music_search.domain.SearchRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, MediaModule::class])
class SearchModule {
    @Provides
    fun provideSearchRepository(
        service: MediaServiceApi,
        @Named("apiKey") apiKey: String,
    ): SearchRepository {
        return SearchRepositoryImpl(service, apiKey)
    }

    @Provides
    @Singleton
    fun providesGetMusicByPrefixUseCase(repository: SearchRepository): GetMusicByPrefixUseCase {
        return GetMusicByPrefixUseCase(repository)
    }
}