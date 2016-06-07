package com.szagurskii.superfaststartup.util;

import android.os.SystemClock;

import java.util.Random;

/**
 * @author Savelii Zagurskii
 */
public class VeryHeavyLibrary {
  public VeryHeavyLibrary(Random random) {
    SystemClock.sleep(random.nextInt(3000));
  }

  public String initializedString() {
    return "Initialized!";
  }
}
