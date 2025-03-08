package com.mediaapp.album.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mediaapp.album.databinding.ActivityAlbumBinding
import com.mediaapp.album.di.AlbumDepsProvider
import com.mediaapp.album.di.AlbumFeatureComponent
import com.mediaapp.core.models.Track

class AlbumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val track = getTrack()
        if (track != null) {
            Glide.with(this).load(track.album_image)
                .placeholder(com.mediaapp.design_system.R.drawable.standard_icon)
                .into(binding.albumImage)
            binding.albumName.text = track.album_name
            binding.artistName.text = track.artist_name
        }
    }

    private fun getTrack(): Track? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("track", Track::class.java)
        } else {
            intent.getParcelableExtra("track")
        }
    }

    private fun initDI() {
        val albumComponent = (application as AlbumDepsProvider).getAlbumComponent()
        albumComponent.inject(this)
        albumComponent.inject()
    }
}
