package com.szagurskii.superfaststartup.util;

import android.os.SystemClock;

import java.util.Random;

/**
 * @author Savelii Zagurskii
 */
public class Heavy2Library {
  public Heavy2Library(Random random) {
    SystemClock.sleep(random.nextInt(2000));
  }

  public String initializedString() {
    return "Initialized " + getClass().getSimpleName();
  }
}
