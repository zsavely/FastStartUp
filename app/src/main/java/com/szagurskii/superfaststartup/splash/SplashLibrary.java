package com.szagurskii.superfaststartup.splash;

import java.util.Locale;
import java.util.UUID;

public class SplashLibrary {
  private static final String TAG = SplashLibrary.class.getSimpleName();

  private int count;

  public SplashLibrary() {
    count = 0;
  }

  public void init() {
    int j = 0;
    // Simulate hard initialization process.
    for (int i = 0; i < 10000000; i++) {
      // Log.d(TAG, String.format("i = %1$s", i));
      // SystemClock.sleep(10);
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;
      j++;

      String uuid = UUID.randomUUID().toString().toUpperCase(Locale.getDefault());
    }
    count = j;
  }

  public String usefulString() {
    return "Useful string. " + getClass().getName();
  }

  public String initializedString() {
    return "Initialized " + getClass().getSimpleName();
  }
}
