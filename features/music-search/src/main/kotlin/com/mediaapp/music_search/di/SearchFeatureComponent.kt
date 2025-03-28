package com.mediaapp.music_search.di

import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.core.utils.PlaylistHostLauncher
import com.mediaapp.music_search.presentation.SearchFragment
import com.mediaapp.music_search.presentation.viewmodel.SearchViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SearchModule::class], dependencies = [MusicSearchDeps::class])
interface SearchFeatureComponent {

    fun inject(searchViewModel: SearchViewModel)
    fun inject(searchViewModel: SearchFragment)

    @Component.Builder
    interface Builder {
        fun deps(deps: MusicSearchDeps): Builder
        fun build(): SearchFeatureComponent
    }
}

interface MusicSearchDeps {
    val musicServiceLauncher: MusicServiceLauncher
    val playlistHostLauncher: PlaylistHostLauncher
}

interface SearchDepsProvider {
    fun getSearchComponent(): SearchFeatureComponent
}