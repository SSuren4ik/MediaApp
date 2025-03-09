package com.mediaapp.album.di

import com.mediaapp.album.presentation.viewmodel.AlbumViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AlbumFeatureModule::class])
interface AlbumFeatureComponent {

    fun inject(viewModel: AlbumViewModel)

    @Component.Builder
    interface Builder {
        fun build(): AlbumFeatureComponent
    }
}

interface AlbumDepsProvider {
    fun getAlbumComponent(): AlbumFeatureComponent
}