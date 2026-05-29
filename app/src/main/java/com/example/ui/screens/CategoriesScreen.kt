package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.viewmodel.MainViewModel

data class CategoryItem(val name: String, val query: String, val icon: ImageVector)

val categories = listOf(
    CategoryItem("World", "general", Icons.Default.Public),
    CategoryItem("Technology", "technology", Icons.Default.Computer),
    CategoryItem("Sport", "sports", Icons.Default.SportsSoccer),
    CategoryItem("Entertainment", "entertainment", Icons.Default.Movie),
    CategoryItem("Business", "business", Icons.Default.Business),
    CategoryItem("Health", "health", Icons.Default.HealthAndSafety),
    CategoryItem("Science", "science", Icons.Default.Science)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(viewModel: MainViewModel, navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Categories") })
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                Card(
                    modifier = Modifier
                        .height(120.dp)
                        .clickable {
                            viewModel.fetchNews(category.query)
                            navController.navigate("home") {
                                popUpTo("home")
                                launchSingleTop = true
                            }
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = category.name,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = category.name, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
