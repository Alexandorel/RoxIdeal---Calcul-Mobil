package com.example.roxideal.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // server address
    private const val BASE_URL = "http://10.0.2.2:8080/"

    @Volatile
    var token: String? = null

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = token?.let {
                chain.request().newBuilder().header("Authorization", "Bearer $it").build()
            } ?: chain.request()
            chain.proceed(request)
        }
        .build()

    val api: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}
