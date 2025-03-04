package com.mediaapp.core.domain

import com.mediaapp.core.data.MediaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaService {

    @GET("tracks")
    suspend fun getPopular(
        @Query("client_id") apiKey: String,
        @Query("format") format: String = "json",
        @Query("boost") boost: String = "popularity_month",
        @Query("limit") limit: Int = 20,
        @Query("include") include: String = "musicinfo",
    ): MediaResponse

    @GET("tracks")
    suspend fun getNewReleases(
        @Query("client_id") apiKey: String,
        @Query("format") format: String = "json",
        @Query("order") order: String = "releasedate_desc",
        @Query("limit") limit: Int = 20,
        @Query("include") include: String = "musicinfo",
        @Query("groupby") groupby: String = "album_id",
    ): MediaResponse

    @GET("tracks")
    suspend fun getTopDownloads(
        @Query("client_id") apiKey: String,
        @Query("format") format: String = "json",
        @Query("order") order: String = "downloads_month_desc",
        @Query("limit") limit: Int = 20,
        @Query("include") include: String = "musicinfo",
    ): MediaResponse
}
