package com.szagurskii.superfaststartup.splash;

import android.os.SystemClock;

import java.util.Random;

/**
 * @author Savelii Zagurskii
 */
public class SplashLibrary {
  public SplashLibrary(Random random) {
    SystemClock.sleep(random.nextInt(3000));
  }

  public String usefulString() {
    return "Useful string. " + getClass().getName();
  }

  public String initializedString() {
    return "Initialized " + getClass().getSimpleName();
  }
}
