package com.kinotir.api

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerApi {

    @GET("/ajax/user.php?action=login&submit=true")
    fun login(@Query("email") email: String, @Query("password") password: String): Single<Response<ResponseBody>>

    @GET("/")
    fun todayFilms(): Single<Response<ResponseBody>>

    @GET("/skoro-v-kino")
    fun soonFilms(): Single<Response<ResponseBody>>

    @GET("{path}")
    fun filmDetails(@Path("path") path: String): Single<Response<ResponseBody>>

    @GET("/lc/")
    fun tickets(): Single<Response<ResponseBody>>

    @GET("/novosti")
    fun newses(): Single<Response<ResponseBody>>

}
