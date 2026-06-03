package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

data class VideoItem(val id: Int, val title: String, val author: String, val imageUrl: String, val likes: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideosScreen() {
    val videos = listOf(
        VideoItem(1, "Breaking News: AI Breakthrough", "@tech_news", "https://images.unsplash.com/photo-1620712943543-bcc4688e7485?q=80&w=1000", "12K"),
        VideoItem(2, "Market Update: Global Trends", "@finance_daily", "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?q=80&w=1000", "8.5K"),
        VideoItem(3, "SpaceX Latest Launch Highlights", "@space_exploration", "https://images.unsplash.com/photo-1517976487492-5750f3195933?q=80&w=1000", "24K"),
        VideoItem(4, "Tech Review: Next Gen Gadgets", "@gadget_guru", "https://images.unsplash.com/photo-1498049794561-7780e7231661?q=80&w=1000", "15K"),
        VideoItem(5, "Climate Summit Key Moments", "@earth_matters", "https://images.unsplash.com/photo-1470071131384-001b85755536?q=80&w=1000", "30K")
    )

    val pagerState = rememberPagerState(pageCount = { videos.size })

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            VideoPlayerItem(video = videos[page])
        }
        
        TopAppBar(
            title = { Text("News Shorts", color = Color.White, fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
    }
}

@Composable
fun VideoPlayerItem(video: VideoItem) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = video.imageUrl,
            contentDescription = "Video Thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.8f
        )
        
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play",
            tint = Color.White.copy(alpha = 0.6f),
            modifier = Modifier.size(80.dp).align(Alignment.Center)
        )
        
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = video.author,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = video.title,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VideoActionItem(icon = Icons.Default.Favorite, text = video.likes)
            Spacer(modifier = Modifier.height(16.dp))
            VideoActionItem(icon = Icons.Default.Comment, text = "Comment")
            Spacer(modifier = Modifier.height(16.dp))
            VideoActionItem(icon = Icons.Default.Share, text = "Share")
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun VideoActionItem(icon: ImageVector, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )
    }
}