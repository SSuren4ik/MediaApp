package com.mediaapp.core.data

data class MediaResponse(
    val results: List<Track>,
)

data class Track(
    val id: String = "",
    val name: String = "",
    val duration: Int = 0,
    val artist_id: String = "",
    val artist_name: String = "",
    val album_name: String = "",
    val album_id: String = "",
    val position: Int = 0,
    val releasedate: String = "",
    val album_image: String = "",
    val audio: String = "",
    val audiodownload: String = "",
    val image: String = "",
    val musicinfo: MusicInfo? = null,
)

data class MusicInfo(
    val tags: Tags,
)

data class Tags(
    val genres: List<String>,
)
