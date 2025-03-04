package com.mediaapp.utils

import android.content.Context
import android.content.Intent
import com.mediaapp.MainActivity
import com.mediaapp.registration.utils.RegistrationRouter

class AppNavigationComponent : RegistrationRouter {
    override fun navigateToMainActivity(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }
}