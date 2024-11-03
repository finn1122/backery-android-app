package com.example.mybakery.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val BASE_URL = "http://192.168.3.30/bakery/api/v1/"

    // Crea un cliente OkHttp con el interceptor
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(LoggingInterceptor()) // Agrega el interceptor aquí
        .build()

    // Inicializa Retrofit con el cliente OkHttp
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient) // Usa el cliente OkHttp configurado
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
