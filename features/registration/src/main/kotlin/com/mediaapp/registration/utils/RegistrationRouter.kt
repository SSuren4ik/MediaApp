package com.mediaapp.registration.utils

import android.content.Context
import com.mediaapp.core.utils.Router

interface RegistrationRouter : Router {
    fun navigateToMainActivity(context: Context)
}