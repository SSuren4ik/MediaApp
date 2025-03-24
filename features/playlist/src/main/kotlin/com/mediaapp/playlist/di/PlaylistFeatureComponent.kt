package com.mediaapp.playlist.di

import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.playlist.presentation.playlist_screen.PlaylistActivity
import com.mediaapp.playlist.presentation.user_playlists.UserPlaylistsFragment
import com.mediaapp.playlist.presentation.user_playlists.viewmodel.PlaylistViewModel
import com.mediaapp.playlist.presentation.user_playlists.viewmodel.UserPlaylistsViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [PlaylistModule::class], dependencies = [PlaylistDeps::class])
interface PlaylistFeatureComponent {

    fun inject(fragment: UserPlaylistsFragment)
    fun inject(userPlaylistsViewModel: UserPlaylistsViewModel)
    fun inject(playlistViewModel: PlaylistViewModel)
    fun inject(activity: PlaylistActivity)

    @Component.Builder
    interface Builder {
        fun deps(deps: PlaylistDeps): Builder
        fun build(): PlaylistFeatureComponent
    }
}

interface PlaylistDeps {
    val musicServiceLauncher: MusicServiceLauncher
}

interface PlaylistComponentProvider {
    fun getPlaylistComponent(): PlaylistFeatureComponent
}