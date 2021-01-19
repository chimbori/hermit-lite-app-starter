package com.chimbori.liteapp.starter

import android.content.Intent.ACTION_VIEW
import com.chimbori.liteapp.starter.HermitIntentStarter.createLiteAppIntent
import org.junit.Assert.assertEquals
import org.junit.Test

class HermitIntentStarterTest {
  @Test
  fun testCreateSimpleLiteAppIntent() {
    val actualIntent = createLiteAppIntent("https://example.org/")
    assertEquals(ACTION_VIEW, actualIntent.action)
    assertEquals("https://example.org/", actualIntent.dataString)
    assertEquals("com.chimbori.hermitcrab/com.chimbori.hermitcrab.WebActivity", actualIntent.component!!.flattenToString())
  }
}