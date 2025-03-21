package com.mediaapp.utils

import android.content.Context
import android.content.Intent
import com.mediaapp.MainActivity
import com.mediaapp.album.presentation.AlbumActivity
import com.mediaapp.core.models.Track
import com.mediaapp.core.utils.AlbumLauncher
import com.mediaapp.registration.utils.RegistrationRouter

class AppNavigationComponent : RegistrationRouter, AlbumLauncher {
    override fun navigateToMainActivity(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }

    override fun launchAlbum(context: Context, track: Track) {
        val intent = Intent(context, AlbumActivity::class.java)
        intent.putExtra("track", track)
        context.startActivity(intent)
    }
}