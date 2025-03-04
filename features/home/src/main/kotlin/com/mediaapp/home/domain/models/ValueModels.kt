package com.mediaapp.home.domain.models

import com.mediaapp.core.data.Track

@JvmInline
value class NewMusic(val value: List<Track>)

@JvmInline
value class PopularMusic(val value: List<Track>)

@JvmInline
value class TopDownloadsMusic(val value: List<Track>)
