package com.szagurskii.superfaststartup.splash;

import android.support.annotation.NonNull;

import com.szagurskii.superfaststartup.util.Heavy2Library;

import java.util.Random;
import java.util.concurrent.Callable;

import dagger.Module;
import dagger.Provides;
import rx.Observable;

/**
 * @author Savelii Zagurskii
 */
@Module
public class SplashModule {
  @Provides @NonNull @SplashScope public Observable<Heavy2Library> veryHeavyLibrary(final Random random) {
    return Observable.fromCallable(new Callable<Heavy2Library>() {
      @Override public Heavy2Library call() throws Exception {
        return new Heavy2Library(random);
      }
    });
  }
}
