package com.example.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.viewmodel.MainViewModel
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.AiSearchScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.VideosScreen

sealed class Screen(val route: String, val title: String, val activeIcon: ImageVector, val inactiveIcon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Categories : Screen("categories", "Categories", Icons.AutoMirrored.Filled.List, Icons.AutoMirrored.Outlined.List)
    object Search : Screen("search", "AI Search", Icons.Filled.Search, Icons.Outlined.Search)
    object Videos : Screen("videos", "Videos", Icons.Filled.PlayArrow, Icons.Outlined.PlayArrow)
    object Favorites : Screen("favorites", "Favorites", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Categories,
    Screen.Search,
    Screen.Videos,
    Screen.Favorites
)

@Composable
fun MainAppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                bottomNavItems.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = { Icon(if (selected) screen.activeIcon else screen.inactiveIcon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(viewModel) }
            composable(Screen.Categories.route) { CategoriesScreen(viewModel, navController) }
            composable(Screen.Search.route) { AiSearchScreen(viewModel) }
            composable(Screen.Videos.route) { VideosScreen() }
            composable(Screen.Favorites.route) { FavoritesScreen(viewModel) }
        }
    }
}
