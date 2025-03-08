package com.mediaapp.di

import android.app.Application
import com.mediaapp.album.di.AlbumFeatureComponent
import com.mediaapp.core.utils.LauncherAlbum
import com.mediaapp.core.utils.Router
import com.mediaapp.home.di.HomeDeps
import com.mediaapp.registration.di.RegistrationDeps
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent : RegistrationDeps, HomeDeps {

    override val styleResources: Int
    override val router: Router
    override val launcherAlbum: LauncherAlbum

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}
