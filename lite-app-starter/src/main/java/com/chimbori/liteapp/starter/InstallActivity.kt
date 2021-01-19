package com.chimbori.liteapp.starter

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.chimbori.liteapp.starter.Constants.HERMIT_DOWNLOAD_LOCATION
import com.chimbori.liteapp.starter.Constants.HERMIT_PACKAGE_NAME

class InstallActivity : Activity(), View.OnClickListener {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_install_dialog)

    findViewById<TextView>(R.id.install_dialog_description).text = if (SDK_INT >= N) {
      Html.fromHtml(getString(R.string.hermit_description), FROM_HTML_MODE_LEGACY)
    } else {
      Html.fromHtml(getString(R.string.hermit_description))
    }
    findViewById<View>(R.id.install_dialog_install_button).setOnClickListener(this)
    findViewById<View>(R.id.install_dialog_cancel_button).setOnClickListener(this)
  }

  override fun onClick(view: View) {
    when (view.id) {
      R.id.install_dialog_install_button -> {
        val wasPlayStoreSuccessful = navigateToPlayStore(this, HERMIT_PACKAGE_NAME)
        if (!wasPlayStoreSuccessful) {
          val wasBrowserNavigationSuccessful = navigateToHermitDownloadSite(this)
          if (!wasBrowserNavigationSuccessful) {
            Toast.makeText(applicationContext, getString(R.string.error_no_app_available), LENGTH_LONG).show()
          }
        }
        finishAndRemoveTask()
      }
      R.id.install_dialog_cancel_button -> finishAndRemoveTask()
    }
  }

  companion object {
    private fun navigateToHermitDownloadSite(activity: Activity): Boolean = try {
      activity.startActivity(
          Intent(ACTION_VIEW).setData(Uri.parse(HERMIT_DOWNLOAD_LOCATION))
              .addFlags(FLAG_ACTIVITY_NO_HISTORY or FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_MULTIPLE_TASK))
      true
    } catch (e: ActivityNotFoundException) {
      false
    }

    private fun navigateToPlayStore(activity: Activity, packageId: String): Boolean {
      return try {
        activity.startActivity(Intent(ACTION_VIEW, Uri.parse("market://details?id=$packageId"))
            .addFlags(FLAG_ACTIVITY_NO_HISTORY or FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_MULTIPLE_TASK))
        true
      } catch (e: ActivityNotFoundException) {
        Toast.makeText(activity.applicationContext, R.string.error_play_store, LENGTH_LONG).show()
        false
      }
    }
  }
}