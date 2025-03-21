package com.mediaapp.playlist.di

import com.mediaapp.playlist.presentation.PlaylistFragment
import com.mediaapp.playlist.presentation.viewmodel.PlaylistViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [PlaylistModule::class])
interface PlaylistFeatureComponent {

    fun inject(fragment: PlaylistFragment)
    fun inject(playlistViewModel: PlaylistViewModel)

    @Component.Builder
    interface Builder {
        fun build(): PlaylistFeatureComponent
    }
}

interface PlaylistComponentProvider {
    fun getPlaylistComponent(): PlaylistFeatureComponent
}