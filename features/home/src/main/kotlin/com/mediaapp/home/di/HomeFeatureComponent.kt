package com.mediaapp.home.di

import com.mediaapp.core.utils.AlbumLauncher
import com.mediaapp.home.presentation.HomeFragment
import com.mediaapp.home.presentation.viewmodel.HomeViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [HomeDeps::class], modules = [HomeFeatureModule::class])
interface HomeFeatureComponent {

    fun inject(viewModel: HomeViewModel)
    fun inject(viewModel: HomeFragment)

    @Component.Builder
    interface Builder {
        fun deps(deps: HomeDeps): Builder
        fun build(): HomeFeatureComponent
    }
}

interface HomeDeps {
    val albumLauncher: AlbumLauncher
}

interface HomeDepsProvider {
    fun getHomeComponent(): HomeFeatureComponent
}