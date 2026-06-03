package com.example.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.components.ArticleCard
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.NewsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val uiState by viewModel.homeState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("GlobalNewsBuzz", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.primary
            ),
            actions = {
                val isDarkModeOverrides = viewModel.isDarkMode.collectAsState().value
                val isSystemDark = androidx.compose.foundation.isSystemInDarkTheme()
                val useDarkTheme = isDarkModeOverrides ?: isSystemDark

                IconButton(onClick = { viewModel.toggleDarkMode(useDarkTheme) }) {
                    Icon(
                        imageVector = if (useDarkTheme) Icons.Filled.WbSunny else Icons.Filled.DarkMode,
                        contentDescription = "Toggle Theme"
                    )
                }
            }
        )
        
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search news...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            shape = MaterialTheme.shapes.medium
        )

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refreshNews() },
            modifier = Modifier.fillMaxSize()
        ) {
            when (val state = uiState) {
                is NewsUiState.Loading -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(5) {
                            com.example.ui.components.SkeletonArticleCard()
                        }
                    }
                }
                is NewsUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.fetchNews("general") }) {
                            Text("Retry")
                        }
                    }
                }
                is NewsUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            com.example.ui.components.TrendsChart()
                        }
                        
                        val filteredArticles = if (searchQuery.isBlank()) {
                            state.articles
                        } else {
                            state.articles.filter {
                                (it.title?.contains(searchQuery, ignoreCase = true) == true) ||
                                (it.description?.contains(searchQuery, ignoreCase = true) == true)
                            }
                        }
                        
                        items(filteredArticles) { article ->
                            ArticleCard(article = article, viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
