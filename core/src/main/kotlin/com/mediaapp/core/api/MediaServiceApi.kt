package com.mediaapp.core.api

import com.mediaapp.core.models.MediaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaServiceApi {

    @GET("tracks")
    suspend fun getPopular(
        @Query("client_id") apiKey: String,
        @Query("format") format: String = "json",
        @Query("boost") boost: String = "popularity_month",
        @Query("limit") limit: Int = 10,
        @Query("include") include: String = "musicinfo",
        @Query("offset") offset: Int = 0,
    ): MediaResponse

    @GET("tracks")
    suspend fun getNewReleases(
        @Query("client_id") apiKey: String,
        @Query("format") format: String = "json",
        @Query("order") order: String = "releasedate_desc",
        @Query("limit") limit: Int = 10,
        @Query("include") include: String = "musicinfo",
        @Query("groupby") groupby: String = "album_id",
        @Query("offset") offset: Int = 0,
    ): MediaResponse

    @GET("tracks")
    suspend fun getTopDownloads(
        @Query("client_id") apiKey: String,
        @Query("format") format: String = "json",
        @Query("order") order: String = "downloads_month_desc",
        @Query("limit") limit: Int = 10,
        @Query("include") include: String = "musicinfo",
        @Query("offset") offset: Int = 0,
    ): MediaResponse

    @GET("tracks")
    suspend fun getAlbumTracksWithLinks(
        @Query("client_id") apiKey: String,
        @Query("album_id") albumId: Int,
        @Query("format") format: String = "json",
        @Query("include") include: String = "musicinfo",
    ): MediaResponse

}
