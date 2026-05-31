package com.example

import android.app.Application
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ui.navigation.MainAppNavigation
import com.example.ui.theme.AppTheme
import com.example.viewmodel.MainViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class AppCrashTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testAppDoesNotCrashOnStart() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = MainViewModel(application)
        
        composeTestRule.setContent {
            AppTheme {
                MainAppNavigation(viewModel = viewModel)
            }
        }
        
        composeTestRule.waitForIdle()
    }
}
