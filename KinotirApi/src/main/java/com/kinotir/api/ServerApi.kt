package com.kinotir.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerApi {

    @GET("/ajax/user.php?action=login&submit=true")
    suspend fun login(@Query("email") email: String, @Query("password") password: String): Response<ResponseBody>

    @GET("/")
    suspend fun todayFilms(): Response<ResponseBody>

    @GET("/skoro-v-kino")
    suspend fun soonFilms(): Response<ResponseBody>

    @GET("{path}")
    suspend fun filmDetails(@Path("path") path: String): Response<ResponseBody>

    @GET("/lc/")
    suspend fun tickets(): Response<ResponseBody>

    @GET("/novosti")
    suspend fun newses(): Response<ResponseBody>

}
