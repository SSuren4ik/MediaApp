package com.mediaapp.di

import android.app.Application
import com.mediaapp.core.utils.Router
import com.mediaapp.registration.di.RegistrationDeps
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent : RegistrationDeps {

    override val styleResources: Int
    override val router: Router

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}
