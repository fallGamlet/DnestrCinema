package com.kinotir.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerApi {

    @GET("/ajax/user.php?action=login&submit=true")
    fun login(@Query("email") email: String, @Query("password") password: String): Single<Response<String>>

    @GET("/")
    fun todayFilms(): Single<Response<String>>

    @GET("/skoro-v-kino")
    fun soonFilms(): Single<Response<String>>

    @GET("{path}")
    fun filmDetails(@Path("path") path: String): Single<Response<String>>

    @GET("/lc/")
    fun tickets(): Single<Response<String>>

    @GET("/novosti")
    fun newses(): Single<Response<String>>

}
