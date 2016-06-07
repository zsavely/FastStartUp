package com.szagurskii.superfaststartup.main;

import android.support.annotation.NonNull;

import com.szagurskii.superfaststartup.util.VeryHeavyLibrary;

import java.util.Random;
import java.util.concurrent.Callable;

import dagger.Module;
import dagger.Provides;
import rx.Observable;

/**
 * @author Savelii Zagurskii
 */
@Module
public class MainModule {
  @Provides @NonNull @MainScope public Random random() {
    return new Random();
  }

  @Provides @NonNull @MainScope public Observable<VeryHeavyLibrary> veryHeavyLibrary(final Random random) {
    return Observable.fromCallable(new Callable<VeryHeavyLibrary>() {
      @Override public VeryHeavyLibrary call() throws Exception {
        return new VeryHeavyLibrary(random);
      }
    });
  }
}
