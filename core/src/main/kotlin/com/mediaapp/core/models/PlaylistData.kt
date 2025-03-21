package com.mediaapp.core.models

data class PlaylistData(
    val name: String = "",
    val image: String = "",
    val tracks: List<Track> = emptyList(),
)
