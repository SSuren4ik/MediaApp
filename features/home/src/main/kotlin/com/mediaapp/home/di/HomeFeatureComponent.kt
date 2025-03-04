package com.mediaapp.home.di

import android.content.Context
import com.mediaapp.core.domain.MediaService
import com.mediaapp.home.presentation.viewmodel.HomeViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [HomeFeatureModule::class])
interface HomeFeatureComponent {
    val service: MediaService
    fun inject(viewModel: HomeViewModel)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): HomeFeatureComponent
    }
}

interface HomeDepsProvider {
    fun getHomeComponent(): HomeFeatureComponent
}