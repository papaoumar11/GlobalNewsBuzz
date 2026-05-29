package com.example

import androidx.test.core.app.ActivityScenario
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.Shadows.shadowOf
import android.os.Looper

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun testMainActivityStarts() {
      ActivityScenario.launch(MainActivity::class.java).use { scenario ->
          scenario.onActivity { activity ->
              shadowOf(Looper.getMainLooper()).idle()
              assertEquals(true, activity != null)
          }
      }
  }
}
