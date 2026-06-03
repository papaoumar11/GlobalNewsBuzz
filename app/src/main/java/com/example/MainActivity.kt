package com.example

import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.navigation.MainAppNavigation
import com.example.ui.theme.AppTheme
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("crash_prefs", Context.MODE_PRIVATE)
        val lastCrash = prefs.getString("last_crash", null)
        
        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            prefs.edit().putString("last_crash", e.stackTraceToString()).commit()
            // Let the default handler do its thing or just exit
            System.exit(1)
        }
        
        enableEdgeToEdge()
        setContent {
            val isDarkModeOverrides = viewModel.isDarkMode.collectAsState().value
            val isSystemDark = androidx.compose.foundation.isSystemInDarkTheme()
            val useDarkTheme = isDarkModeOverrides ?: isSystemDark

            AppTheme(darkTheme = useDarkTheme) {
                if (lastCrash != null) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        androidx.compose.foundation.layout.Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            Text("CRASH:", color = MaterialTheme.colorScheme.error)
                            Text(lastCrash, color = MaterialTheme.colorScheme.error)
                            androidx.compose.material3.Button(onClick = { prefs.edit().clear().apply() }) {
                                Text("Clear Crash")
                            }
                        }
                    }
                } else {
                    MainAppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}
