package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.api.Article

@Entity(tableName = "favorites")
data class ArticleEntity(
    @PrimaryKey val url: String,
    val sourceName: String?,
    val author: String?,
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
    val timestamp: Long = System.currentTimeMillis()
)

fun Article.toEntity(): ArticleEntity {
    return ArticleEntity(
        url = url,
        sourceName = source?.name,
        author = author,
        title = title,
        description = description,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

fun ArticleEntity.toArticle(): Article {
    return Article(
        source = com.example.api.Source(name = sourceName ?: "Unknown"),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}
