package com.example.streamingservice.data.api_clients

import com.example.streamingservice.domain.entities.SignInRequest
import com.example.streamingservice.domain.entities.SignUpRequest
import com.example.streamingservice.domain.entities.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiClientBackend {

    @POST("/api/auth/signup")
    fun userSignUp(@Body signUpRequest: SignUpRequest): Call<ResponseBody>

    @POST("/api/auth/signin")
    fun userSignIn(@Body signInRequest: SignInRequest): Call<ResponseBody>

    @Multipart
    @POST("/api/user/update")
    fun changeUserData(
        @Part("user") newUser: RequestBody,
        @Part file: MultipartBody.Part?,
        @Header("Authorization") accessToken: String
    ): Call<ResponseBody>

    @GET("/api/user/collection/playlists")
    fun getUserPlaylist(@Header("Authorization") accessToken: String): Call<ResponseBody>

    @GET("/api/playlist/all")
    fun getAllPlaylist(): Call<ResponseBody>

    @GET("/api/audiobook/all")
    fun getAllAudioBook(@Header("Authorization") accessToken: String): Call<ResponseBody>
}