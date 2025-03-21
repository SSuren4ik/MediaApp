package com.mediaapp.home.domain.models

import com.mediaapp.core.models.Track

@JvmInline
value class NewMusic(val value: List<Track> = emptyList())

@JvmInline
value class PopularMusic(val value: List<Track> = emptyList())

@JvmInline
value class TopDownloadsMusic(val value: List<Track> = emptyList())
