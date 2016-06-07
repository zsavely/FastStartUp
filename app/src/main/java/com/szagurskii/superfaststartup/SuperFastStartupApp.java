package com.szagurskii.superfaststartup;

import android.app.Application;
import android.content.Context;

import com.szagurskii.superfaststartup.main.DaggerMainComponent;
import com.szagurskii.superfaststartup.main.MainComponent;
import com.szagurskii.superfaststartup.main.MainModule;
import com.szagurskii.superfaststartup.splash.DaggerSplashComponent;
import com.szagurskii.superfaststartup.splash.SplashComponent;
import com.szagurskii.superfaststartup.splash.SplashModule;

/**
 * @author Savelii Zagurskii
 */
public class SuperFastStartupApp extends Application {
  private ApplicationComponent applicationComponent;
  private MainComponent mainComponent;
  private SplashComponent splashComponent;

  @Override public void onCreate() {
    super.onCreate();

    applicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(this))
        .build();
  }

  public ApplicationComponent appComponent() {
    return applicationComponent;
  }

  public MainComponent mainComponent() {
    if (mainComponent == null) {
      mainComponent = DaggerMainComponent.builder()
          .applicationComponent(applicationComponent)
          .mainModule(new MainModule())
          .build();
    }
    return mainComponent;
  }

  public SplashComponent splashComponent() {
    if (splashComponent == null) {
      splashComponent = DaggerSplashComponent.builder()
          .applicationComponent(applicationComponent)
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

  public static SuperFastStartupApp app(Context context) {
    return (SuperFastStartupApp) context.getApplicationContext();
  }
}
