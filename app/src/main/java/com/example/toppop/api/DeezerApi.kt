package com.example.toppop.api

import com.example.toppop.models.TopChart
import retrofit2.Response
import retrofit2.http.GET


interface DeezerApi {
    @GET("chart")
    suspend fun getTopTen() : Response<TopChart>
}