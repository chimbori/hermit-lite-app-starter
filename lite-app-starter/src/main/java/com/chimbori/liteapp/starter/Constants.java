package com.chimbori.liteapp.starter;

public class Constants {
  /**
   * Well-known special name for the extra that contains the URI of the manifest.json.
   */
  static final String EXTRA_KEY_MANIFEST = "manifest";

  /**
   * Package name of the Hermit app.
   */
  static final String HERMIT_PACKAGE_NAME = "com.chimbori.hermitcrab";

  /**
   * Component name of the deep-linked Activity in Hermit to use to open a Lite App.
   */
  static final String HERMIT_ACTIVITY_NAME = "com.chimbori.hermitcrab.WebActivity";

  /**
   * URL where Hermit can be downloaded from directly as an APK. Used on devices where
   * Play Store is not available.
   */
  static final String HERMIT_DOWNLOAD_LOCATION = "https://hermit.chimbori.com/downloads";
}
