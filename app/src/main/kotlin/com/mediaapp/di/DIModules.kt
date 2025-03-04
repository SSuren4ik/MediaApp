package com.mediaapp.di

import com.mediaapp.R
import com.mediaapp.core.utils.Router
import com.mediaapp.utils.AppNavigationComponent
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
}