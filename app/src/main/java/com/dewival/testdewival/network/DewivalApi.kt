package com.dewival.testdewival.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface DewivalApi {

    @Headers("Accept: application/json")
    @POST("api/login/")
    suspend fun getToken(@Body registrationBody: RegistrationBody): String

    @Multipart
    @POST("api/sendfile/")
    suspend fun sendFile(@Header("Authorization") authorization:String,
                         @Part imageFile: MultipartBody.Part,
                         @Part realmFile: MultipartBody.Part)

}