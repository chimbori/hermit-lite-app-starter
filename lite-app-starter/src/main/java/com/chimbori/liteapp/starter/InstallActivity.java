package com.chimbori.liteapp.starter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

public class InstallActivity extends Activity implements View.OnClickListener {
  private static final String TAG = "InstallActivity";

  private Context context;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = getApplicationContext();
    setContentView(R.layout.activity_install_dialog);

    TextView descriptionTextView = findViewById(R.id.install_dialog_description);
    String descriptionHtml = getString(R.string.hermit_description);
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
      descriptionTextView.setText(Html.fromHtml(descriptionHtml, Html.FROM_HTML_MODE_LEGACY));
    } else {
      descriptionTextView.setText(Html.fromHtml(descriptionHtml));
    }
    findViewById(R.id.install_dialog_install_button).setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.install_dialog_install_button) {
      boolean wasPlayStoreSuccessful = navigateToPlayStore(this, Constants.HERMIT_PACKAGE_NAME);
      if (!wasPlayStoreSuccessful) {
        boolean wasBrowserNavigationSuccessful = navigateToHermitDownloadSite(this);
        if (!wasBrowserNavigationSuccessful) {
          Toast.makeText(context, context.getString(R.string.error_no_app_available), Toast.LENGTH_LONG).show();
        }
      }
      finishAndRemoveTask();
    } else if (view.getId() == R.id.install_dialog_cancel_button) {
      finishAndRemoveTask();
    }
  }

  private boolean navigateToHermitDownloadSite(@NonNull Activity activity) {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW)
        .setData(Uri.parse(Constants.HERMIT_DOWNLOAD_LOCATION))
        .addFlags(FLAG_ACTIVITY_NO_HISTORY | FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_MULTIPLE_TASK);
    try {
      activity.startActivity(browserIntent);
      return true;

    } catch (ActivityNotFoundException e) {
      return false;
    }
  }

  private static boolean navigateToPlayStore(@NonNull Activity activity, @NonNull String packageId) {
    Context context = activity.getApplicationContext();
    Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageId))
        .addFlags(FLAG_ACTIVITY_NO_HISTORY | FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_MULTIPLE_TASK);
    try {
      activity.startActivity(playStoreIntent);
      return true;
    } catch (ActivityNotFoundException anfe) {
      Toast.makeText(context, R.string.error_play_store, Toast.LENGTH_LONG).show();
      return false;
    }
  }
}
