package com.mediaapp.home.data

import com.mediaapp.core.api.MediaServiceApi
import com.mediaapp.core.models.NetworkException
import com.mediaapp.core.models.UnknownException
import com.mediaapp.home.domain.models.NewMusic
import com.mediaapp.home.domain.models.PopularMusic
import com.mediaapp.home.domain.models.TopDownloadsMusic
import com.mediaapp.home.domain.repository.HomeMediaRepository
import retrofit2.HttpException
import java.io.IOException

class HomeMediaRepositoryImpl(
    private val service: MediaServiceApi,
    private val apiKey: String,
) : HomeMediaRepository {

    override suspend fun getPopularMusic(offset: Int, limit: Int): PopularMusic {
        return try {
            val musicData = service.getPopular(apiKey, offset = offset, limit = limit)
            PopularMusic(musicData.results)
        } catch (e: IOException) {
            throw NetworkException(e.message.toString(), e)
        } catch (e: HttpException) {
            throw e
        } catch (e: Exception) {
            throw UnknownException(e.message.toString(), e)
        } catch (e: IOException) {
            throw NetworkException(e.message.toString(), e)
        }
    }

    override suspend fun getNewMusic(offset: Int, limit: Int): NewMusic {
        return try {
            val musicData = service.getNewReleases(apiKey, offset = offset, limit = limit)
            NewMusic(musicData.results)
        } catch (e: IOException) {
            throw NetworkException(e.message.toString(), e)
        } catch (e: HttpException) {
            throw e
        } catch (e: Exception) {
            throw UnknownException(e.message.toString(), e)
        }
    }

    override suspend fun getTopDownloadsMusic(offset: Int, limit: Int): TopDownloadsMusic {
        return try {
            val musicData = service.getTopDownloads(apiKey, offset = offset, limit = limit)
            TopDownloadsMusic(musicData.results)
        } catch (e: IOException) {
            throw NetworkException(e.message.toString(), e)
        } catch (e: HttpException) {
            throw e
        } catch (e: Exception) {
            throw UnknownException(e.message.toString(), e)
        }
    }
}