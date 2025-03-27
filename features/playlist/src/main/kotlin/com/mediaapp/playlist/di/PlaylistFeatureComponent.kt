package com.mediaapp.playlist.di

import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.playlist.presentation.playlist_screen.CurrentPlaylistActivity
import com.mediaapp.playlist.presentation.user_playlists.UserPlaylistsFragment
import com.mediaapp.playlist.presentation.user_playlists.viewmodel.CurrentPlaylistViewModel
import com.mediaapp.playlist.presentation.user_playlists.viewmodel.UserPlaylistsViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [PlaylistModule::class], dependencies = [PlaylistDeps::class])
interface PlaylistFeatureComponent {

    fun inject(fragment: UserPlaylistsFragment)
    fun inject(userPlaylistsViewModel: UserPlaylistsViewModel)
    fun inject(currentPlaylistViewModel: CurrentPlaylistViewModel)
    fun inject(activity: CurrentPlaylistActivity)

    @Component.Builder
    interface Builder {
        fun deps(deps: PlaylistDeps): Builder
        fun build(): PlaylistFeatureComponent
    }
}

interface PlaylistDeps {
    val musicServiceLauncher: MusicServiceLauncher
}

interface PlaylistDepsProvider {
    fun getPlaylistComponent(): PlaylistFeatureComponent
}