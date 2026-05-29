package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM favorites ORDER BY timestamp DESC")
    fun getFavorites(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(article: ArticleEntity)

    @Query("DELETE FROM favorites WHERE url = :url")
    suspend fun deleteFavorite(url: String)
}
