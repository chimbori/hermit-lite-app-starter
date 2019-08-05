package com.chimbori.liteapp.starter;

import android.content.Intent;

import org.junit.Test;

import static com.chimbori.liteapp.starter.HermitIntentStarter.createSimpleLiteAppIntent;
import static org.junit.Assert.assertEquals;

public class HermitIntentStarterTest {
  @Test
  public void testCreateSimpleLiteAppIntent() {
    Intent actualIntent = createSimpleLiteAppIntent("https://example.org/");
    assertEquals(Intent.ACTION_VIEW, actualIntent.getAction());
    assertEquals("https://example.org/", actualIntent.getDataString());
    assertEquals("com.chimbori.hermitcrab/com.chimbori.hermitcrab.WebActivity", actualIntent.getComponent().flattenToString());
  }
}
