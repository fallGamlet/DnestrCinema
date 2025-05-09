package com.kinotir.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class ApiFactory(
    private val appName: String = "",
    debug: Boolean = false,
    serverUrl: String? = null
) {

    private val baseUrl = serverUrl ?: "http://kinotir.md"
    val api: KinotirApi

    init {
        val httpClient = createHttpClient(debug)
        api = KinotirApiImpl(
            serverApi = createServerApi(httpClient)
        )
    }

    private fun createServerApi(httpClient: OkHttpClient): ServerApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .build()

        return retrofit.create(ServerApi::class.java)
    }

    private fun createHttpClient(debug: Boolean = false): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()

        clientBuilder.addInterceptor(::intercept)

        if (debug) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(interceptor)
        }

        return clientBuilder.build()
    }

    private fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val builder = original.newBuilder()
            .header("User-Agent", appName)

        return chain.proceed(builder.build())
    }
}
