package com.szagurskii.superfaststartup;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
  @NonNull
  private final SuperFastStartupApp application;

  public ApplicationModule(@NonNull SuperFastStartupApp application) {
    this.application = application;
  }

  @Provides @NonNull @Singleton
  public SuperFastStartupApp provideApp() {
    return application;
  }
}
