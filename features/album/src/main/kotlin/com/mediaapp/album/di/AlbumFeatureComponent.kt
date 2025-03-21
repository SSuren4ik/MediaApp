package com.mediaapp.album.di

import com.mediaapp.album.presentation.AlbumActivity
import com.mediaapp.album.presentation.viewmodel.AlbumViewModel
import com.mediaapp.core.utils.MusicServiceLauncher
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [AlbumDeps::class], modules = [AlbumFeatureModule::class])
interface AlbumFeatureComponent {

    fun inject(viewModel: AlbumViewModel)
    fun inject(viewModel: AlbumActivity)

    @Component.Builder
    interface Builder {
        fun deps(deps: AlbumDeps): Builder
        fun build(): AlbumFeatureComponent
    }
}

interface AlbumDeps {
    val musicServiceLauncher: MusicServiceLauncher
}

interface AlbumDepsProvider {
    fun getAlbumComponent(): AlbumFeatureComponent
}