package com.szagurskii.superfaststartup.splash;

/** An interface to hide the possible consumer. */
public interface OnInitCallbacks {
  /** Called when {@link SplashLibrary} has been successfully initialized. */
  void onSuccess(SplashLibrary splashLibrary);

  /** Called when library initialization failed. */
  void onFailure(Throwable e);
}
