package com.mediaapp.music_search.di

import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.core.utils.PlaylistHostLauncher
import com.mediaapp.music_search.presentation.SearchFragment
import com.mediaapp.music_search.presentation.viewmodel.SearchViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SearchModule::class], dependencies = [SearchDeps::class])
interface SearchFeatureComponent {

    fun inject(searchViewModel: SearchViewModel)
    fun inject(searchViewModel: SearchFragment)

    @Component.Builder
    interface Builder {
        fun deps(deps: SearchDeps): Builder
        fun build(): SearchFeatureComponent
    }
}

interface SearchDeps {
    val musicServiceLauncher: MusicServiceLauncher
    val playlistHostLauncher: PlaylistHostLauncher
}

interface SearchDepsProvider {
    fun getSearchComponent(): SearchFeatureComponent
}