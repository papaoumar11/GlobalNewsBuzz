package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideosScreen() {
    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        TopAppBar(
            title = { Text("News Shorts", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
        )
        
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Video",
                    modifier = Modifier.size(64.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Video buzz feed coming soon!",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "(Full YouTube Shorts / TikTok integration placeholder)",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
