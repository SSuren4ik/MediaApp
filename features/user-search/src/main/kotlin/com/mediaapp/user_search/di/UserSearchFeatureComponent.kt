package com.mediaapp.user_search.di

import com.mediaapp.core.utils.SelectedUserPlaylistsLauncher
import com.mediaapp.user_search.presentation.search_screen.UserSearchActivity
import com.mediaapp.user_search.presentation.search_screen.viewmodel.UserSearchViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [UserSearchModule::class], dependencies = [UserSearchDeps::class])
interface UserSearchFeatureComponent {
    fun inject(activity: UserSearchActivity)
    fun inject(viewModel: UserSearchViewModel)

    @Component.Builder
    interface Builder {
        fun deps(deps: UserSearchDeps):Builder
        fun build(): UserSearchFeatureComponent
    }
}

interface UserSearchDeps {
    val selectedUserPlaylistsLauncher: SelectedUserPlaylistsLauncher
}

interface UserSearchDepsProvider {
    fun getUserSearchComponent(): UserSearchFeatureComponent
}