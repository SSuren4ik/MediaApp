package com.mediaapp.di

import android.app.Application
import com.mediaapp.album.di.AlbumDeps
import com.mediaapp.core.utils.AlbumLauncher
import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.core.utils.PlaylistHostLauncher
import com.mediaapp.core.utils.PlaylistLauncher
import com.mediaapp.core.utils.Router
import com.mediaapp.core.utils.SelectedUserPlaylistsLauncher
import com.mediaapp.core.utils.UserSearchLauncher
import com.mediaapp.home.di.HomeDeps
import com.mediaapp.music_search.di.MusicSearchDeps
import com.mediaapp.music_service.presentation.MusicService
import com.mediaapp.playlist.di.PlaylistDeps
import com.mediaapp.registration.di.RegistrationDeps
import com.mediaapp.user_search.di.UserSearchDeps
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent : RegistrationDeps, HomeDeps, AlbumDeps, PlaylistDeps, MusicSearchDeps,
    UserSearchDeps {

    override val styleResources: Int
    override val router: Router
    override val albumLauncher: AlbumLauncher
    override val musicServiceLauncher: MusicServiceLauncher
    override val userSearchLauncher: UserSearchLauncher
    override val selectedUserPlaylistsLauncher: SelectedUserPlaylistsLauncher
    override val playlistHostLauncher: PlaylistHostLauncher
    override val playlistLauncher: PlaylistLauncher
    override val musicService: MusicService

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}
