package com.example.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

        Box(modifier = Modifier.fillMaxSize()) {
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
                        modifier = Modifier.align(Alignment.Center).padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.fetchNews("general") }) {
                            Text("Retry")
                        }
                    }
                }
                is NewsUiState.Success -> {
                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
                        onRefresh = { viewModel.refreshNews() },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                com.example.ui.components.TrendsChart()
                            }
                            items(state.articles) { article ->
                                ArticleCard(article = article, viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
