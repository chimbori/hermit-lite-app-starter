package com.chimbori.liteapp.starter

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.chimbori.liteapp.starter.Constants.HERMIT_ACTIVITY_NAME
import com.chimbori.liteapp.starter.Constants.HERMIT_PACKAGE_NAME

object HermitIntentStarter {
  private fun createLiteAppIntent(startUrl: String) =
      Intent(Intent.ACTION_VIEW, Uri.parse(startUrl)).setClassName(HERMIT_PACKAGE_NAME, HERMIT_ACTIVITY_NAME)

  fun launchLiteApp(activity: Activity, startUrl: String) = try {
    activity.startActivity(createLiteAppIntent(startUrl))
  } catch (e: ActivityNotFoundException) {
    activity.startActivity(Intent(activity, InstallActivity::class.java))
  }
}