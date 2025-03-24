package com.mediaapp.core.models

data class PlaylistData(
    val playlistName: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val image: String = "",
    val tracks: List<Track> = emptyList(),
)
