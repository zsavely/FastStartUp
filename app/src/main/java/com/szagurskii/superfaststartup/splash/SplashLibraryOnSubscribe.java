package com.szagurskii.superfaststartup.splash;

import dagger.Lazy;
import rx.Observable;
import rx.Subscriber;

/**
 * @author Savelii Zagurskii
 */
final class SplashLibraryOnSubscribe implements Observable.OnSubscribe<SplashLibrary> {

  /** Lazy instance of the library. */
  private Lazy<SplashLibrary> splashLazy;

  public SplashLibraryOnSubscribe(Lazy<SplashLibrary> splashLazy) {
    this.splashLazy = splashLazy;
  }

  @Override public void call(Subscriber<? super SplashLibrary> subscriber) {
    try {
      subscriber.onStart();
      SplashLibrary splashLibrary = splashLazy.get();
      subscriber.onNext(splashLibrary);
      subscriber.onCompleted();
    } catch (Throwable throwable) {
      subscriber.onError(throwable);
    }
  }
}
