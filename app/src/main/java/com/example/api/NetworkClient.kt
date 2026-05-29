package com.example.api

import com.example.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {
    private const val NEWS_BASE_URL = "https://newsapi.org/"
    private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/"

    private val json = Json { ignoreUnknownKeys = true; isLenient = true; explicitNulls = false }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val newsApiService: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(NEWS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(NewsApiService::class.java)
    }

    val geminiApiService: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(GEMINI_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(GeminiApiService::class.java)
    }
}
