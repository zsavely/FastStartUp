package com.szagurskii.superfaststartup.splash;

import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Named;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.AsyncEmitter;
import rx.Observable;
import rx.Single;
import rx.functions.Action1;
import rx.functions.Func0;

@Module
public class SplashModule {
  public static final String SPLASH_ACTIVITY = "SplashActivity";
  public static final String OBSERVABLE_JSON_STRING = "observable_json_string";
  public static final String OBSERVABLE_SPLASH_LIBRARY = "observable_splash_library";
  public static final String OBSERVABLE_SPLASH_LIBRARY_FROM_CALLABLE = "observable_splash_library_from_callable";
  public static final String OBSERVABLE_SPLASH_LIBRARY_ASYNC = "observable_splash_library_async";

  @Provides @NonNull @SplashScope @Named(SPLASH_ACTIVITY)
  public AtomicBoolean initialized() {
    return new AtomicBoolean(false);
  }

  @Provides @NonNull @SplashScope
  public SplashLibrary splashLibrary() {
    SplashLibrary splashLibrary = new SplashLibrary();
    splashLibrary.init();
    return splashLibrary;
  }

  @Provides @NonNull @SplashScope
  public OkHttpClient okHttpClient() {
    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addNetworkInterceptor(new StethoInterceptor())
        .build();
    return okHttpClient;
  }

  @Provides @NonNull @SplashScope
  public Retrofit retrofit(@NonNull OkHttpClient okHttpClient) {
    Retrofit retrofit = new Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .baseUrl("http://jsonplaceholder.typicode.com")
        .validateEagerly(true)
        .build();
    return retrofit;
  }

  @Provides @NonNull @SplashScope
  public FakeInterface fakeInterface(@NonNull Retrofit retrofit) {
    return retrofit.create(FakeInterface.class);
  }

  @Provides @NonNull @SplashScope @Named(OBSERVABLE_JSON_STRING)
  public Observable<String> jsonStringObservable(@NonNull FakeInterface fakeInterface) {
    return fakeInterface.firstPost();
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
        emitter.onNext(splashLazy.get());
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
