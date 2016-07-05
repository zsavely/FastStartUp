package com.szagurskii.superfaststartup.main;

import android.os.SystemClock;

import java.util.Random;

/**
 * @author Savelii Zagurskii
 */
public class MainLibrary {
  public MainLibrary(Random random) {
    SystemClock.sleep(random.nextInt(3000));
  }

  public String initializedString() {
    return "Initialized " + getClass().getSimpleName();
  }
}
