package com.szagurskii.superfaststartup.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.szagurskii.superfaststartup.FastStartupApp;
import com.szagurskii.superfaststartup.R;
import com.szagurskii.superfaststartup.main.MainActivity;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Lazy;
import dagger.internal.Preconditions;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class SplashActivity extends AppCompatActivity {
  private static final String TAG = SplashActivity.class.getSimpleName();

  /** Lazy instance which will be used when the {@link SplashLibrary} is initialized. */
  @Inject Lazy<SplashLibrary> splashLibraryLazy;

  /** Observable which will emit an item when fully initialized.* {@link rx.Single} can also be used. */
  @Inject Observable<SplashLibrary> splashLibraryObservable;

  /** Is {@link SplashLibrary} initialized? */
  @Inject @Named(SplashModule.SPLASH_ACTIVITY) AtomicBoolean initialized;

  /** Subscription to unsubscribe in onStop(). */
  private Subscription subscription;

  /** Observer which receives the initialization result. */
  private OnInitObserver onInitObserver;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Theme background is already shown at the moment
    // but we can also show a ProgressBar which will appear on top.
    setContentView(R.layout.activity_splash);

    // Inject the {@link SplashActivity#splashLibraryObservable}.
    FastStartupApp.app(this).splashComponent().inject(this);

    // Check that the injection is successful.
    Preconditions.checkNotNull(splashLibraryLazy);
    Preconditions.checkNotNull(splashLibraryObservable);
    Preconditions.checkNotNull(initialized);

    onInitObserver = new OnInitObserver(this);

    if (initialized.get()) {
      usefulMethod(this, splashLibraryLazy.get());
    } else {
      // Create subscription.
      subscription = splashLibraryObservable
          // Init library on another thread.
          .subscribeOn(Schedulers.computation())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(onInitObserver);
    }
  }

  @Override protected void onStop() {
    super.onStop();

    // Unsubscribe, so we don't open another activity after exiting.
    if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
    // In current example SplashLibrary initializes 5 seconds and our Observer references the SplashActivity.
    // We must release the Activity reference in order to avoid memory leaking.
    onInitObserver.releaseActivity();
  }

  @Override protected void onDestroy() {
    super.onDestroy();

    // Optional: release the resources which were acquired in {@link SplashModule}.
    // Note: if we want to survive rotation, we should not release the component.
    // Note: if we want fast subsequent app start-up, we should not release the component.
    // FastStartupApp.app(this).releaseSplashComponent();
  }

  /** Show toast, start new activity and finish current activity. */
  private static void usefulMethod(@NonNull SplashActivity splashActivity, @NonNull SplashLibrary splashLibrary) {
    final String initialized = splashLibrary.initializedString();
    Toast.makeText(splashActivity, initialized, Toast.LENGTH_SHORT).show();

    Intent intent = new Intent(splashActivity, MainActivity.class);
    intent.putExtra(MainActivity.EXTRA_USEFUL_STRING_KEY, splashLibrary.usefulString());
    splashActivity.startActivity(intent);

    splashActivity.finish();
  }

  // Yes, activity can become null but at that point in time we will have already unsubscribed from this Observer.
  @SuppressWarnings("ConstantConditions")
  private static class OnInitObserver implements Observer<SplashLibrary> {
    @Nullable private SplashActivity splashActivity;

    public OnInitObserver(@NonNull SplashActivity splashActivity) {
      this.splashActivity = splashActivity;
    }

    @Override public void onCompleted() {
    }

    @Override public void onError(Throwable e) {
      Log.d(TAG, "Library initialization failed.", e);
      Toast.makeText(splashActivity, R.string.error_fatal, Toast.LENGTH_SHORT).show();
      splashActivity.finish();
    }

    @Override public void onNext(SplashLibrary splashLibrary) {
      splashActivity.initialized.set(true);
      usefulMethod(splashActivity, splashLibrary);
    }

    private void releaseActivity() {
      splashActivity = null;
    }
  }
}
