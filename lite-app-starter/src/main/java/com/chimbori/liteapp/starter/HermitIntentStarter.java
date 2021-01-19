package com.chimbori.liteapp.starter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

public class HermitIntentStarter {
  @NonNull
  public static Intent createLiteAppIntent(@NonNull String startUrl) {
    Uri startUri = Uri.parse(startUrl);
    return new Intent(Intent.ACTION_VIEW, startUri)
        .setClassName(Constants.HERMIT_PACKAGE_NAME, Constants.HERMIT_ACTIVITY_NAME);
  }

  public static void launchLiteApp(@NonNull Activity activity, @NonNull String startUrl) {
    try {
      activity.startActivity(createLiteAppIntent(startUrl));
    } catch (ActivityNotFoundException e) {
      activity.startActivity(new Intent(activity, InstallActivity.class));
    }
  }
}
