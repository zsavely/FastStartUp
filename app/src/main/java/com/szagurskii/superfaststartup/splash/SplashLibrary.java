package com.szagurskii.superfaststartup.splash;

import android.os.SystemClock;
import android.util.Log;

public class SplashLibrary {
  private static final String TAG = SplashLibrary.class.getSimpleName();

  public SplashLibrary() {

    // Simulate hard initialization process.
    for (int i = 0; i < 5; i++) {
      Log.d(TAG, String.format("i = %1$s", i));
      SystemClock.sleep(1000);
    }
  }

  public String usefulString() {
    return "Useful string. " + getClass().getName();
  }

  public String initializedString() {
    return "Initialized " + getClass().getSimpleName();
  }
}
