package com.mediaapp.playlist.presentation.playlist_host

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.mediaapp.core.models.Track
import com.mediaapp.playlist.R
import com.mediaapp.playlist.databinding.ActivityPlaylistHostBinding
import com.mediaapp.playlist.presentation.user_playlists.UserPlaylistsFragment

class PlaylistHostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlaylistHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val track = getTrack()

        track?.let {
            val fragment = UserPlaylistsFragment.newInstance(true, it)
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

    private fun getTrack(): Track? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("track", Track::class.java)
        } else {
            intent.getParcelableExtra("track")
        }
    }
}