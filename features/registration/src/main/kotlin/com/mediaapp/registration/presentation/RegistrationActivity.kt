package com.mediaapp.registration.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mediaapp.core.utils.Router
import com.mediaapp.registration.R
import com.mediaapp.registration.databinding.ActivityRegistrationBinding
import com.mediaapp.registration.di.RegistrationDeps
import com.mediaapp.registration.di.RegistrationDepsProvider
import com.mediaapp.registration.utils.RegistrationRouter
import javax.inject.Inject

class RegistrationActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var registrationDeps: RegistrationDeps

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        initDI()
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()

        if (savedInstanceState == null) {
            splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
                splashScreenViewProvider.view.animate().alpha(0f).setDuration(500).withEndAction {
                    setTheme(registrationDeps.styleResources)
                    splashScreenViewProvider.remove()
                }.start()
            }
        }

        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val fragment = LoginFragment()
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

    private fun initDI() {
        val component = (application as RegistrationDepsProvider).getRegistrationComponent()
        component.inject(this)
    }

    fun onRegistrationComplete() {
        (router as RegistrationRouter).navigateToMainActivity(this)
        finish()
    }
}
