package com.szagurskii.superfaststartup;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.szagurskii.superfaststartup.main.DaggerMainComponent;
import com.szagurskii.superfaststartup.main.MainComponent;
import com.szagurskii.superfaststartup.main.MainModule;
import com.szagurskii.superfaststartup.splash.DaggerSplashComponent;
import com.szagurskii.superfaststartup.splash.SplashComponent;
import com.szagurskii.superfaststartup.splash.SplashModule;

/**
 * @author Savelii Zagurskii
 */
public final class FastStartupApp extends Application {

  /** Common application component. */
  private ApplicationComponent applicationComponent;

  /** Activity-related components. */
  private MainComponent mainComponent;
  private SplashComponent splashComponent;

  @Override public void onCreate() {
    super.onCreate();
    StrictMode.enableDefaults();

    applicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(this))
        .build();
  }

  @Nullable public ApplicationComponent appComponent() {
    return applicationComponent;
  }

  @NonNull public MainComponent mainComponent() {
    if (mainComponent == null) {
      mainComponent = DaggerMainComponent.builder()
          .applicationComponent(appComponent())
          .mainModule(new MainModule())
          .build();
    }
    return mainComponent;
  }

  @NonNull public SplashComponent splashComponent() {
    if (splashComponent == null) {
      splashComponent = DaggerSplashComponent.builder()
          .applicationComponent(appComponent())
          .splashModule(new SplashModule())
          .build();
    }
    return splashComponent;
  }

  public void releaseMainComponent() {
    mainComponent = null;
  }

  public void releaseSplashComponent() {
    splashComponent = null;
  }

  @NonNull public static FastStartupApp app(@NonNull Context context) {
    return (FastStartupApp) context.getApplicationContext();
  }
}
