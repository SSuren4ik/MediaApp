package com.mediaapp.playlist.di

import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.core.utils.PlaylistHostLauncher
import com.mediaapp.core.utils.PlaylistLauncher
import com.mediaapp.core.utils.UserSearchLauncher
import com.mediaapp.playlist.presentation.current_user_playlists.UserPlaylistsFragment
import com.mediaapp.playlist.presentation.current_user_playlists.viewmodel.UserPlaylistsViewModel
import com.mediaapp.playlist.presentation.playlist_screen.CurrentPlaylistActivity
import com.mediaapp.playlist.presentation.playlist_screen.viewmodel.CurrentPlaylistViewModel
import com.mediaapp.playlist.presentation.selected_user_playlists.SelectedUserPlaylistsActivity
import com.mediaapp.playlist.presentation.selected_user_playlists.viewmodel.SelectedUserPlaylistsViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [PlaylistModule::class], dependencies = [PlaylistDeps::class])
interface PlaylistFeatureComponent {

    fun inject(fragment: UserPlaylistsFragment)
    fun inject(userPlaylistsViewModel: UserPlaylistsViewModel)
    fun inject(currentPlaylistViewModel: CurrentPlaylistViewModel)
    fun inject(activity: CurrentPlaylistActivity)
    fun inject(selectedUserPlaylistsActivity: SelectedUserPlaylistsActivity)
    fun inject(viewModel: SelectedUserPlaylistsViewModel)

    @Component.Builder
    interface Builder {
        fun deps(deps: PlaylistDeps): Builder
        fun build(): PlaylistFeatureComponent
    }
}

interface PlaylistDeps {
    val musicServiceLauncher: MusicServiceLauncher
    val userSearchLauncher: UserSearchLauncher
    val playlistHostLauncher: PlaylistHostLauncher
    val playlistLauncher: PlaylistLauncher
}

interface PlaylistDepsProvider {
    fun getPlaylistComponent(): PlaylistFeatureComponent
}