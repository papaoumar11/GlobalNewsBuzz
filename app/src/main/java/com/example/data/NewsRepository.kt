package com.example.data

import com.example.BuildConfig
import com.example.api.Article
import com.example.api.NetworkClient
import com.example.api.Content
import com.example.api.GenerateContentRequest
import com.example.api.Part
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch

class NewsRepository(private val articleDao: ArticleDao) {
    private val newsApi by lazy { NetworkClient.newsApiService }
    private val geminiApi by lazy { NetworkClient.geminiApiService }
    
    // In production, warn user about keys. Using safe fallback if empty.
    private val newsApiKey = BuildConfig.NEWS_API_KEY.takeIf { it.isNotBlank() && it != "MY_NEWS_API_KEY" } ?: "demo_key"
    private val geminiApiKey = BuildConfig.GEMINI_API_KEY

    suspend fun getTopHeadlines(category: String? = null): List<Article> {
        return try {
            val response = newsApi.getTopHeadlines(category = category, apiKey = newsApiKey)
            response.articles
        } catch (e: Throwable) {
            e.printStackTrace()
            getMockArticles(category) // Fallback for testing when key is missing or invalid
        }
    }

    suspend fun searchNewsWithAI(query: String): String {
        return try {
            val res = geminiApi.generateContent(
                apiKey = geminiApiKey,
                request = GenerateContentRequest(
                    contents = listOf(Content(listOf(Part("Please provide a brief, professional summary of the latest news about: $query"))))
                )
            )
            res.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No results found."
        } catch (e: Throwable) {
            e.printStackTrace()
            "AI summary unavailable. Please check your Gemini API key, or it might be another configuration issue: ${e.message}"
        }
    }
    
    fun getFavorites(): Flow<List<Article>> {
        return articleDao.getFavorites()
            .map { entities -> entities.map { it.toArticle() } }
            .catch { e ->
                e.printStackTrace()
                emit(emptyList())
            }
    }
    
    suspend fun toggleFavorite(article: Article, isFav: Boolean) {
        if (isFav) {
            articleDao.deleteFavorite(article.url)
        } else {
            articleDao.insertFavorite(article.toEntity())
        }
    }

    private fun getMockArticles(category: String?): List<Article> {
        val catStr = category ?: "General"
        return listOf(
            Article(source=com.example.api.Source(name="TechCrunch"), title="New AI Model Released $catStr", description="Google just announced a new AI model for mobile devices.", url="https://example.com/1", publishedAt="2026-05-29T10:00:00Z"),
            Article(source=com.example.api.Source(name="BBC News"), title="Global Markets Rally $catStr", description="Stock markets around the world hit record highs today.", url="https://example.com/2", publishedAt="2026-05-29T09:00:00Z"),
            Article(source=com.example.api.Source(name="The Verge"), title="Next Gen Smartphone Review $catStr", description="Is the new foldable worth the price? We find out.", url="https://example.com/3", publishedAt="2026-05-29T08:00:00Z", urlToImage="https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?q=80&w=600&auto=format&fit=crop")
        )
    }
}
