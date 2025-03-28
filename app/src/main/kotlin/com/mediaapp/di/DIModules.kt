package com.mediaapp.di

import com.mediaapp.R
import com.mediaapp.core.utils.AlbumLauncher
import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.core.utils.PlaylistHostLauncher
import com.mediaapp.core.utils.PlaylistLauncher
import com.mediaapp.core.utils.Router
import com.mediaapp.core.utils.SelectedUserPlaylistsLauncher
import com.mediaapp.core.utils.UserSearchLauncher
import com.mediaapp.utils.AppNavigationComponent
import com.mediaapp.utils.MusicServiceLauncherImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ResourcesModule::class, UtilsModule::class])
class AppModule

@Module
class ResourcesModule {
    @Provides
    fun provideSplashScreenStyle(): Int {
        return R.style.Theme_MediaApp
    }
}

@Module
class UtilsModule {
    @Provides
    @Singleton
    fun provideRouter(): Router {
        return AppNavigationComponent()
    }

    @Provides
    @Singleton
    fun provideAlbumLauncher(): AlbumLauncher {
        return AppNavigationComponent()
    }

    @Provides
    @Singleton
    fun provideLauncherMusicService(): MusicServiceLauncher {
        return MusicServiceLauncherImpl()
    }

    @Provides
    @Singleton
    fun providesPlaylistHostLauncher(): PlaylistHostLauncher {
        return AppNavigationComponent()
    }

    @Provides
    @Singleton
    fun provideUserSearchLauncher(): UserSearchLauncher {
        return AppNavigationComponent()
    }

    @Provides
    @Singleton
    fun provideSelectedUserPlaylistsLauncher(): SelectedUserPlaylistsLauncher {
        return AppNavigationComponent()
    }

    @Provides
    @Singleton
    fun providesPlaylistLauncher(): PlaylistLauncher {
        return AppNavigationComponent()
    }
}