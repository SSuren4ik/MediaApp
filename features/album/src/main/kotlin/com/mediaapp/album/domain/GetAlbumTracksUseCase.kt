package com.mediaapp.album.domain

class GetAlbumTracksUseCase(private val repository: AlbumMediaRepository) {
    suspend fun execute(data: AlbumData): NetworkRequest {
        return repository.getAlbumTracks(data)
    }
}