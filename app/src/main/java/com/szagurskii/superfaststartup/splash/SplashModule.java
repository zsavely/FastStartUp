package com.szagurskii.superfaststartup.splash;

import android.support.annotation.NonNull;

import javax.inject.Named;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import rx.AsyncEmitter;
import rx.Observable;
import rx.Single;
import rx.functions.Action1;
import rx.functions.Func0;

@Module
public class SplashModule {
  public static final String OBSERVABLE_SPLASH_LIBRARY = "observable_splash_library";
  public static final String OBSERVABLE_SPLASH_LIBRARY_VIA_CREATE = "observable_splash_library_via_create";
  public static final String OBSERVABLE_SPLASH_LIBRARY_FROM_CALLABLE = "observable_splash_library_from_callable";
  public static final String OBSERVABLE_SPLASH_LIBRARY_ASYNC = "observable_splash_library_async";

  @Provides @NonNull @SplashScope
  public SplashLibrary splashLibrary() {
    return new SplashLibrary();
  }

  @Provides @NonNull @SplashScope @Named(OBSERVABLE_SPLASH_LIBRARY)
  public Observable<SplashLibrary> splashLibraryObservable(final Lazy<SplashLibrary> splashLazy) {
    return Observable.defer(new Func0<Observable<SplashLibrary>>() {
      @Override public Observable<SplashLibrary> call() {
        return Observable.just(splashLazy.get());
      }
    });
  }

  /** Another possible way to emit initialized {@link SplashLibrary}. */
  @Provides @NonNull @SplashScope @Named(OBSERVABLE_SPLASH_LIBRARY_VIA_CREATE)
  public Observable<SplashLibrary> splashLibraryObservableViaCreate(final Lazy<SplashLibrary> splashLazy) {
    return Observable.create(new SplashLibraryOnSubscribe(splashLazy));
  }

  /** Another possible way to emit initialized {@link SplashLibrary}. */
  @Provides @NonNull @SplashScope @Named(OBSERVABLE_SPLASH_LIBRARY_FROM_CALLABLE)
  public Observable<SplashLibrary> splashLibraryObservableFromCallable(final Lazy<SplashLibrary> splashLazy) {
    return Observable.fromCallable(new Func0<SplashLibrary>() {
      @Override public SplashLibrary call() {
        return splashLazy.get();
      }
    });
  }

  /** Another possible way to emit initialized {@link SplashLibrary}. */
  @Provides @NonNull @SplashScope @Named(OBSERVABLE_SPLASH_LIBRARY_ASYNC)
  public Observable<SplashLibrary> splashLibraryObservableAsync(final Lazy<SplashLibrary> splashLazy) {
    return Observable.fromAsync(new Action1<AsyncEmitter<SplashLibrary>>() {
      @Override public void call(AsyncEmitter<SplashLibrary> emitter) {
        try {
          emitter.onNext(splashLazy.get());
          emitter.onCompleted();
        } catch (Throwable throwable) {
          emitter.onError(throwable);
        }
      }
    }, AsyncEmitter.BackpressureMode.NONE);
  }

  /** Another possible way to emit initialized {@link SplashLibrary}. */
  @Provides @NonNull @SplashScope
  public Single<SplashLibrary> splashLibrarySingle(final Lazy<SplashLibrary> splashLazy) {
    return Single.defer(new Func0<Single<SplashLibrary>>() {
      @Override public Single<SplashLibrary> call() {
        return Single.just(splashLazy.get());
      }
    });
  }
}
