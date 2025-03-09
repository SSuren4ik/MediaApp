package com.mediaapp.album.presentation

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.bumptech.glide.Glide
import com.mediaapp.album.databinding.ActivityAlbumBinding
import com.mediaapp.album.di.AlbumDepsProvider
import com.mediaapp.album.domain.AlbumData
import com.mediaapp.album.domain.NetworkRequest
import com.mediaapp.album.presentation.viewmodel.AlbumViewModel
import com.mediaapp.core.models.Track
import kotlinx.coroutines.launch

class AlbumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumBinding
    private val viewModel: AlbumViewModel by viewModels()
    private val adapter = AlbumRecyclerViewAdapter()
    private val diffCallback = AlbumDiffCallback()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        initDI()
        setInsets()
        initRecyclerView()
        observeAlbumTracks()
        getAlbumTracks()
    }

    private fun getAlbumTracks() {
        val track = getTrack()
        if (track != null) {
            val albumData = AlbumData(track.album_name)
            showAlbumData(track)
            viewModel.getTracks(albumData)
        }
    }

    private fun observeAlbumTracks() {
        lifecycleScope.launch {
            viewModel.responseStatus.collect { result ->
                when (result) {
                    is NetworkRequest.ErrorConnect -> showToast(result.message)
                    is NetworkRequest.NormalConnect -> {
                        updateData(result.tracks)
                    }
                }
            }
        }
    }

    private fun updateData(tracks: List<Track>) {
        adapter.setData(tracks, diffCallback)
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(ItemSpacingDecorator(10))
    }

    private fun showAlbumData(track: Track) {
        Glide.with(this).load(track.album_image)
            .placeholder(com.mediaapp.design_system.R.drawable.standard_icon)
            .into(binding.albumImage)
        binding.albumName.text = track.album_name
        binding.artistName.text = track.artist_name
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
        albumComponent.inject(viewModel)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.albumImage) { view, insets ->
            val innerPadding = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(0, innerPadding.top, 0, 0)
            insets
        }
    }
}
