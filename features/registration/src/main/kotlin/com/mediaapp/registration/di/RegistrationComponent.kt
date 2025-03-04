package com.mediaapp.registration.di

import com.mediaapp.core.utils.Router
import com.mediaapp.registration.presentation.RegistrationActivity
import com.mediaapp.registration.presentation.viewmodel.LoginViewModel
import com.mediaapp.registration.presentation.viewmodel.SignUpViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RegistrationModule::class], dependencies = [RegistrationDeps::class])
interface RegistrationComponent {

    fun inject(activity: RegistrationActivity)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(signUpViewModel: SignUpViewModel)

    @Component.Builder
    interface Builder {
        fun deps(deps: RegistrationDeps): Builder
        fun build(): RegistrationComponent
    }
}

interface RegistrationDeps {
    val styleResources: Int
    val router: Router
}

interface RegistrationDepsProvider {
    fun getRegistrationComponent(): RegistrationComponent
}