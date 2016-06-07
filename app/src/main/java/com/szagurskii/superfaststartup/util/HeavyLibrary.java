package com.szagurskii.superfaststartup.util;

import android.os.SystemClock;

import java.util.Random;

/**
 * @author Savelii Zagurskii
 */
public class HeavyLibrary {
  public HeavyLibrary(Random random) {
    SystemClock.sleep(random.nextInt(3000));
  }

  public String initializedString() {
    return "Initialized!";
  }
}
