package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiSearchScreen(viewModel: MainViewModel) {
    var query by remember { mutableStateOf("") }
    val aiResult by viewModel.aiSearchState.collectAsState()
    val isLoading by viewModel.isAiLoading.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("AI News Search") },
            actions = {
                Icon(Icons.Default.AutoAwesome, contentDescription = "AI", modifier = Modifier.padding(end = 16.dp), tint = MaterialTheme.colorScheme.primary)
            }
        )
        
        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("What happened in...") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { viewModel.searchWithAI(query) }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (aiResult.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                        Text(
                            text = "AI Summary", 
                            style = MaterialTheme.typography.titleMedium, 
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(text = aiResult, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    Text("Ask our AI to summarize the latest news on any topic.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
