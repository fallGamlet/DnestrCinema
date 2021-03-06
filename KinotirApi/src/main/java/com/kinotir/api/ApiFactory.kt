package com.kinotir.api

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class ApiFactory(
    private val appName: String = "",
    debug: Boolean = false,
    serverUrl: String? = null
) {

    private val baseUrl = serverUrl ?: "http://kinotir.md"
    val api: KinotirApi

    init {
        val serverApi = createServerApi(debug)
        api = KinotirApiImpl(serverApi)
    }

    private fun createServerApi(debug: Boolean = false): ServerApi {
        val clientBuilder = OkHttpClient.Builder()

        clientBuilder.addInterceptor(::intercept)

        if (debug) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(interceptor)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(ServerApi::class.java)
    }

    private fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val builder = original.newBuilder()
            .header("User-Agent", appName)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")

        return chain.proceed(builder.build())
    }
}
