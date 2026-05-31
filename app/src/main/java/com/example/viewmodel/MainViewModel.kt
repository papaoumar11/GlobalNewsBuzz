package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.Article
import com.example.data.AppDatabase
import com.example.data.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NewsRepository(AppDatabase.getDatabase(application).articleDao())
    
    private val _homeState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val homeState: StateFlow<NewsUiState> = _homeState.asStateFlow()

    private val _aiSearchState = MutableStateFlow<String>("")
    val aiSearchState: StateFlow<String> = _aiSearchState.asStateFlow()
    
    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    val favorites: StateFlow<List<Article>> = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteUrls: StateFlow<Set<String>> = repository.getFavorites()
        .map { articles -> articles.map { it.url }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    init {
        fetchNews("general")
    }

    fun fetchNews(category: String) {
        viewModelScope.launch {
            _homeState.value = NewsUiState.Loading
            try {
                val articles = repository.getTopHeadlines(if (category == "general") null else category)
                _homeState.value = NewsUiState.Success(articles)
            } catch (e: Throwable) {
                _homeState.value = NewsUiState.Error("Failed to fetch news: ${e.message}")
            }
        }
    }

    fun searchWithAI(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _isAiLoading.value = true
            try {
                _aiSearchState.value = repository.searchNewsWithAI(query)
            } catch (e: Throwable) {
                _aiSearchState.value = "Error: ${e.message}"
            } finally {
                _isAiLoading.value = false
            }
        }
    }

    fun toggleFavorite(article: Article, isCurrentlyFav: Boolean) {
        viewModelScope.launch {
            try {
                repository.toggleFavorite(article, isCurrentlyFav)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}
