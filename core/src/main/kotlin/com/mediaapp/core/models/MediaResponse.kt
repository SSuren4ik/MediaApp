package com.mediaapp.core.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class MediaResponse(
    val results: List<Track>,
)

@Parcelize
data class Track(
    val id: String = "",
    val name: String = "",
    val duration: Int = 0,
    val artist_id: Int = 0,
    val artist_name: String = "",
    val album_name: String = "",
    val album_id: Int = 0,
    val position: Int = 0,
    val releasedate: String = "",
    val album_image: String = "",
    val audio: String = "",
    val audiodownload: String = "",
    val image: String = "",
    val musicinfo: MusicInfo = MusicInfo(),
) : Parcelable

@Parcelize
data class MusicInfo(
    val tags: Tags = Tags(),
) : Parcelable

@Parcelize
data class Tags(
    val genres: List<String> = emptyList(),
) : Parcelable
