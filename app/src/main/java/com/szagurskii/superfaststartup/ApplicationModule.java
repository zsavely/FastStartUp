package com.szagurskii.superfaststartup;

import android.support.annotation.NonNull;

import java.util.Random;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
  @NonNull private final FastStartupApp application;

  public ApplicationModule(@NonNull FastStartupApp application) {
    this.application = application;
  }

  @Provides @NonNull @Singleton public FastStartupApp provideApp() {
    return application;
  }

  @Provides @NonNull @Singleton public Random random() {
    return new Random();
  }
}
