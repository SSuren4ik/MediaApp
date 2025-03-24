package com.mediaapp.di

import android.app.Application
import com.mediaapp.album.di.AlbumDeps
import com.mediaapp.core.utils.AlbumLauncher
import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.core.utils.Router
import com.mediaapp.home.di.HomeDeps
import com.mediaapp.playlist.di.PlaylistDeps
import com.mediaapp.registration.di.RegistrationDeps
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent : RegistrationDeps, HomeDeps, AlbumDeps, PlaylistDeps {

    override val styleResources: Int
    override val router: Router
    override val albumLauncher: AlbumLauncher
    override val musicServiceLauncher: MusicServiceLauncher

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}
