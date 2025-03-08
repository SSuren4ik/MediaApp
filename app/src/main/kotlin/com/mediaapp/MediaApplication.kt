package com.mediaapp

import android.app.Application
import com.mediaapp.album.di.AlbumDepsProvider
import com.mediaapp.album.di.AlbumFeatureComponent
import com.mediaapp.album.di.DaggerAlbumFeatureComponent
import com.mediaapp.core.utils.ResourceProvider
import com.mediaapp.di.AppComponent
import com.mediaapp.di.DaggerAppComponent
import com.mediaapp.home.di.DaggerHomeFeatureComponent
import com.mediaapp.home.di.HomeDepsProvider
import com.mediaapp.home.di.HomeFeatureComponent
import com.mediaapp.registration.di.DaggerRegistrationComponent
import com.mediaapp.registration.di.RegistrationComponent
import com.mediaapp.registration.di.RegistrationDepsProvider

class MediaApplication : Application(), RegistrationDepsProvider, ResourceProvider,
    HomeDepsProvider, AlbumDepsProvider {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }

    override fun getRegistrationComponent(): RegistrationComponent {
        return DaggerRegistrationComponent.builder().deps(appComponent).build()
    }

    override fun getHomeComponent(): HomeFeatureComponent {
        return DaggerHomeFeatureComponent.builder().deps(appComponent).build()
    }

    override fun getAlbumComponent(): AlbumFeatureComponent {
        return DaggerAlbumFeatureComponent.builder().build()
    }
}