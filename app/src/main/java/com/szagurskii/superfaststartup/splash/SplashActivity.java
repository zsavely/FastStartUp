package com.szagurskii.superfaststartup.splash;

import android.app.Activity;
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

public final class SplashActivity extends AppCompatActivity implements OnInitCallbacks {
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
      openAndFinish(this, splashLibraryLazy.get());
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

  @Override public void onSuccess(SplashLibrary splashLibrary) {
    // The initialization went successful, we can set the global variable to true.
    initialized.set(true);

    // Open new activity and finish current.
    openAndFinish(this, splashLibrary);
  }

  @Override public void onFailure(Throwable e) {
    Toast.makeText(this, R.string.error_fatal, Toast.LENGTH_SHORT).show();
    finish();
  }

  /** Show toast, start new activity and finish current activity. */
  private static void openAndFinish(@NonNull Activity activity, @NonNull SplashLibrary splashLibrary) {
    final String initialized = splashLibrary.initializedString();
    Toast.makeText(activity, initialized, Toast.LENGTH_SHORT).show();

    Intent intent = new Intent(activity, MainActivity.class);
    intent.putExtra(MainActivity.EXTRA_USEFUL_STRING, splashLibrary.usefulString());
    activity.startActivity(intent);

    activity.finish();
  }

  // Yes, activity can become null but at that point in time we will have already unsubscribed from this Observer.
  @SuppressWarnings("ConstantConditions")
  private static final class OnInitObserver implements Observer<SplashLibrary> {
    @Nullable private OnInitCallbacks onInitCallbacks;

    OnInitObserver(@NonNull OnInitCallbacks onInitCallbacks) {
      this.onInitCallbacks = onInitCallbacks;
    }

    @Override public void onCompleted() {
    }

    @Override public void onError(Throwable e) {
      Log.d(TAG, "Library initialization failed.", e);
      onInitCallbacks.onFailure(e);
    }

    @Override public void onNext(SplashLibrary splashLibrary) {
      onInitCallbacks.onSuccess(splashLibrary);
    }

    private void releaseActivity() {
      onInitCallbacks = null;
    }
  }
}
