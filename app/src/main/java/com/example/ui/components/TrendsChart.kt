package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class TrendData(val keyword: String, val popularity: Float)

@Composable
fun TrendsChart(modifier: Modifier = Modifier) {
    val data = listOf(
        TrendData("AI", 0.9f),
        TrendData("Space", 0.7f),
        TrendData("Economy", 0.5f),
        TrendData("Sports", 0.8f),
        TrendData("Tech", 1.0f)
    )

    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Text(
            text = "Top Trending Keywords (24h)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (selectedIndex != null) {
            val selected = data[selectedIndex!!]
            Text(
                text = "${selected.keyword}: ${(selected.popularity * 100).toInt()}% Search Surge",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            val defaultBarColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            val selectedBarColor = MaterialTheme.colorScheme.primary
            val axisColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)

            Canvas(modifier = Modifier
                .fillMaxSize()
                .pointerInput(data.size) {
                    detectTapGestures { offset ->
                        val canvasWidth = size.width
                        val canvasHeight = size.height
                        val barSpacing = 16.dp.toPx()
                        val totalBars = data.size
                        val availableWidth = canvasWidth - (barSpacing * (totalBars - 1))
                        val barWidth = availableWidth / totalBars

                        val tappedIndex = ((offset.x) / (barWidth + barSpacing)).toInt()
                        if (tappedIndex in data.indices) {
                            selectedIndex = tappedIndex
                        }
                    }
                }
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val barSpacing = 16.dp.toPx()
                val totalBars = data.size
                val availableWidth = canvasWidth - (barSpacing * (totalBars - 1))
                val barWidth = availableWidth / totalBars

                // Draw X Axis
                drawLine(
                    color = axisColor,
                    start = Offset(0f, canvasHeight),
                    end = Offset(canvasWidth, canvasHeight),
                    strokeWidth = 2.dp.toPx()
                )

                // Draw bars
                data.forEachIndexed { index, trend ->
                    val barHeight = canvasHeight * trend.popularity
                    val xPos = index * (barWidth + barSpacing)
                    val yPos = canvasHeight - barHeight
                    
                    val color = if (selectedIndex == index) selectedBarColor else defaultBarColor

                    drawRoundRect(
                        color = color,
                        topLeft = Offset(xPos, yPos),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.forEach { trend ->
                Text(
                    text = trend.keyword,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
