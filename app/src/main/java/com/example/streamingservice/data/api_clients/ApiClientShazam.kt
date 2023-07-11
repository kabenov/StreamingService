package com.example.streamingservice.data.api_clients

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiClientShazam {

    @Headers("X-RapidAPI-Key: 0f331a41abmsh808c052f15003e7p15704fjsn1b227e31bb38", "X-RapidAPI-Host: shazam-core.p.rapidapi.com")
    @GET("/v1/charts/country")
    fun getChartByCountry(@Query("country_code") country_code: String): Call<ResponseBody>


    @Headers("X-RapidAPI-Key: 0f331a41abmsh808c052f15003e7p15704fjsn1b227e31bb38", "X-RapidAPI-Host: shazam-core.p.rapidapi.com")
    @GET("/v1/search/multi")
    fun multiSearch(
        @Query("query") query: String,
        @Query("search_type") search_type: String = "SONGS_ARTISTS"
    ): Call<ResponseBody>

}