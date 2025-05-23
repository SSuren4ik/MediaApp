package com.mediaapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.mediaapp.album.di.AlbumDepsProvider
import com.mediaapp.album.di.AlbumFeatureComponent
import com.mediaapp.album.di.DaggerAlbumFeatureComponent
import com.mediaapp.core.utils.ResourceProvider
import com.mediaapp.di.AppComponent
import com.mediaapp.di.DaggerAppComponent
import com.mediaapp.home.di.DaggerHomeFeatureComponent
import com.mediaapp.home.di.HomeDepsProvider
import com.mediaapp.home.di.HomeFeatureComponent
import com.mediaapp.music_search.di.DaggerSearchFeatureComponent
import com.mediaapp.music_search.di.SearchDepsProvider
import com.mediaapp.music_search.di.SearchFeatureComponent
import com.mediaapp.playlist.di.DaggerPlaylistFeatureComponent
import com.mediaapp.playlist.di.PlaylistDepsProvider
import com.mediaapp.playlist.di.PlaylistFeatureComponent
import com.mediaapp.registration.di.DaggerRegistrationComponent
import com.mediaapp.registration.di.RegistrationComponent
import com.mediaapp.registration.di.RegistrationDepsProvider
import com.mediaapp.user_search.di.DaggerUserSearchFeatureComponent
import com.mediaapp.user_search.di.UserSearchDepsProvider
import com.mediaapp.user_search.di.UserSearchFeatureComponent

class MediaApplication : Application(), RegistrationDepsProvider, ResourceProvider,
    HomeDepsProvider, AlbumDepsProvider, PlaylistDepsProvider, SearchDepsProvider,
    UserSearchDepsProvider {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        val channel = NotificationChannel(
            "running_channel", "Running", NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onTerminate() {
        super.onTerminate()
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.deleteNotificationChannel("running_channel")
    }

    override fun getRegistrationComponent(): RegistrationComponent {
        return DaggerRegistrationComponent.builder().deps(appComponent).build()
    }

    override fun getHomeComponent(): HomeFeatureComponent {
        return DaggerHomeFeatureComponent.builder().deps(appComponent).build()
    }

    override fun getAlbumComponent(): AlbumFeatureComponent {
        return DaggerAlbumFeatureComponent.builder().deps(appComponent).build()
    }

    override fun getPlaylistComponent(): PlaylistFeatureComponent {
        return DaggerPlaylistFeatureComponent.builder().deps(appComponent).build()
    }

    override fun getSearchComponent(): SearchFeatureComponent {
        return DaggerSearchFeatureComponent.builder().deps(appComponent).build()
    }

    override fun getUserSearchComponent(): UserSearchFeatureComponent {
        return DaggerUserSearchFeatureComponent.builder().deps(appComponent).build()
    }
}