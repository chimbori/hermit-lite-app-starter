package com.chimbori.liteapp.starter

import android.app.Activity
import android.os.Bundle
import com.chimbori.liteapp.starter.HermitIntentStarter.launchLiteApp

/**
 * This is the Simple version of the APK where you only need to specify a Name, URL, and an Icon.
 *
 *
 * The main code for this app. No need to make any changes here. Things you need to change:
 * - Change the package name in `AndroidManifest.xml`.
 * - Change `start_url` in `res/strings.xml` to point to your Lite App.
 * - Change `app_name` in `res/strings.xml` to the name of your Lite App.
 * - Add any translations required for the `app_name`.
 * - Change the icon in `res/mipmap/ic_launcher.png`. This will be used for your Android native app.
 * - Change the adaptive icons in `res/drawable/ic_launcher_background` &
 * `res/drawable/ic_launcher_foreground`.
 *
 *
 * Then, recompile, sign with your own certificate, and publish your own APK.
 */
class IntentStarterActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    launchLiteApp(this, getString(R.string.start_url))
    finish()
  }
}