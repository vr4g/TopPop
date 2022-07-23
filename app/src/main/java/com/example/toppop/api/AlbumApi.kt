package com.example.toppop.api

import com.example.toppop.models.Album
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface AlbumApi {
    @GET("album/{album_id}")
    suspend fun getAlbumData(@Path(value = "album_id", encoded = false) key: Int?): Response<Album>
}