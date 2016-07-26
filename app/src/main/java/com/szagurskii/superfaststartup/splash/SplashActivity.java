package com.szagurskii.superfaststartup.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.szagurskii.superfaststartup.FastStartupApp;
import com.szagurskii.superfaststartup.R;
import com.szagurskii.superfaststartup.main.MainActivity;

import java.lang.ref.WeakReference;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.internal.Preconditions;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.szagurskii.superfaststartup.splash.SplashModule.OBSERVABLE_SPLASH_LIBRARY;

public final class SplashActivity extends AppCompatActivity implements OnInitCallbacks {
  private static final String TAG = SplashActivity.class.getSimpleName();

  /** Observable which will emit an item when fully initialized. {@link rx.Single} can also be used. */
  @Inject @Named(OBSERVABLE_SPLASH_LIBRARY) Observable<SplashLibrary> splashLibraryObservable;

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
    Preconditions.checkNotNull(splashLibraryObservable);

    onInitObserver = new OnInitObserver(this);
  }

  @Override protected void onStart() {
    super.onStart();

    // Create subscription.
    subscription = splashLibraryObservable
        // Init library on another thread.
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onInitObserver);
  }

  @Override protected void onStop() {
    super.onStop();

    // Unsubscribe, so we don't open another activity after exiting.
    if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();

    // In current example SplashLibrary initializes 5 seconds and our Observer references the SplashActivity.
    // We must release the Activity reference in order to avoid memory leaking.
    onInitObserver.releaseListener();

    // Clear all references.
    onInitObserver = null;
    subscription = null;

    // Optional: release the resources which were acquired in {@link SplashModule}.
    // Note: if we want to survive rotation, we should not release the component.
    // Note: if we want fast subsequent app start-up, we should not release the component.
    // FastStartupApp.app(this).releaseSplashComponent();
  }

  @Override public void onSuccess(SplashLibrary splashLibrary) {
    // Open new activity and finish current.
    openMainAndFinish(splashLibrary);
  }

  @Override public void onFailure(Throwable e) {
    Toast.makeText(this, R.string.error_fatal, Toast.LENGTH_SHORT).show();
    finish();
  }

  /** Show toast, start new activity and finish current activity. */
  private void openMainAndFinish(@NonNull SplashLibrary splashLibrary) {
    final String initialized = splashLibrary.initializedString();
    Toast.makeText(this, initialized, Toast.LENGTH_SHORT).show();

    Intent intent = new Intent(this, MainActivity.class);
    intent.putExtra(MainActivity.EXTRA_USEFUL_STRING, splashLibrary.usefulString());
    startActivity(intent);

    finish();
  }

  // Yes, onInitCallbacks can become null but at that point in time we will have already unsubscribed from this Observer.
  @SuppressWarnings("ConstantConditions")
  private static final class OnInitObserver implements Observer<SplashLibrary> {
    @NonNull private WeakReference<OnInitCallbacks> onInitCallbacks;

    OnInitObserver(@NonNull OnInitCallbacks onInitCallbacks) {
      this.onInitCallbacks = new WeakReference<>(onInitCallbacks);
    }

    @Override public void onCompleted() {
    }

    @Override public void onError(Throwable e) {
      Log.d(TAG, "Library initialization failed.", e);
      onInitCallbacks.get().onFailure(e);
    }

    @Override public void onNext(SplashLibrary splashLibrary) {
      onInitCallbacks.get().onSuccess(splashLibrary);
    }

    private void releaseListener() {
      onInitCallbacks.clear();
    }
  }
}
