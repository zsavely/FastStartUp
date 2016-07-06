package com.szagurskii.superfaststartup.splash;

import android.support.annotation.NonNull;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Named;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import rx.Observable;
import rx.functions.Func0;

@Module
public class SplashModule {
  public static final String SPLASH_ACTIVITY = "SplashActivity";

  @Provides @NonNull @SplashScope @Named(SPLASH_ACTIVITY)
  public AtomicBoolean initialized() {
    return new AtomicBoolean(false);
  }

  @Provides @NonNull @SplashScope
  public SplashLibrary splashLibrary() {
    return new SplashLibrary();
  }

  @Provides @NonNull @SplashScope
  public Observable<SplashLibrary> splashLibraryObservable(final Lazy<SplashLibrary> splashLibraryLazy) {
    return Observable.defer(new Func0<Observable<SplashLibrary>>() {
      @Override public Observable<SplashLibrary> call() {
        return Observable.just(splashLibraryLazy.get());
      }
    });
  }
}
