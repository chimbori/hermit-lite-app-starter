package com.chimbori.liteapp.starter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

public class HermitIntentStarter {
  @NonNull
  public static Intent createSimpleLiteAppIntent(@NonNull String startUrl) {
    Uri startUri = Uri.parse(startUrl);
    return new Intent(Intent.ACTION_VIEW, startUri)
        .setClassName(Constants.HERMIT_PACKAGE_NAME, Constants.HERMIT_ACTIVITY_NAME);
  }

  @NonNull
  public static Intent createLiteAppIntent(@NonNull Context context, @NonNull String startUrl) {
    Uri startUri = Uri.parse(startUrl);
    Uri manifestUri = new Uri.Builder()
        .scheme("content")
        .authority(context.getPackageName() + ".assetprovider")
        .appendEncodedPath("manifest.json")
        .build();
    return new Intent(Intent.ACTION_VIEW, startUri)
        .setClassName(Constants.HERMIT_PACKAGE_NAME, Constants.HERMIT_ACTIVITY_NAME)
        .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        .putExtra(Constants.EXTRA_KEY_MANIFEST, manifestUri);
  }

  public static void launchLiteApp(@NonNull Activity activity, @NonNull String startUrl) {
    try {
      activity.startActivity(createSimpleLiteAppIntent(startUrl));
    } catch (ActivityNotFoundException e) {
      activity.startActivity(new Intent(activity, InstallActivity.class));
    }
  }
}
