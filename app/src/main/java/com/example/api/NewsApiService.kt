package com.example.api

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

@Serializable
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

@Serializable
data class Article(
    val source: Source?,
    val author: String? = null,
    val title: String?,
    val description: String? = null,
    val url: String,
    val urlToImage: String? = null,
    val publishedAt: String?,
    val content: String? = null
)

@Serializable
data class Source(
    val id: String? = null,
    val name: String
)

interface NewsApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("category") category: String? = null,
        @Query("apiKey") apiKey: String
    ): NewsResponse

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String,
        @Query("sortBy") sortBy: String = "publishedAt"
    ): NewsResponse
}
