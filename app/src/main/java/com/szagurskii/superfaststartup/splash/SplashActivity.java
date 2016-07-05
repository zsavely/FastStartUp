package com.szagurskii.superfaststartup.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.szagurskii.superfaststartup.FastStartupApp;
import com.szagurskii.superfaststartup.R;
import com.szagurskii.superfaststartup.main.MainActivity;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class SplashActivity extends AppCompatActivity {
  private static final String TAG = SplashActivity.class.getSimpleName();

  /**
   * Observable which will emit an item when fully initialized.
   * {@link rx.Single} can also be used.
   */
  @Inject Observable<SplashLibrary> splashLibrary;

  /** Subscription to unsubscribe in onStop(). */
  private Subscription subscription;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Theme background is already shown at the moment
    // but we can also show a ProgressBar which will appear on top.
    setContentView(R.layout.activity_splash);

    // Inject the {@link SplashActivity#splashLibrary}.
    FastStartupApp.app(this).splashComponent().inject(this);

    // Check that we successfully injected the {@code splashLibrary}.
    Preconditions.checkNotNull(splashLibrary);

    // Create subscription.
    subscription = splashLibrary
        // Init library on another thread.
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<SplashLibrary>() {
          //@formatter:off
          @Override public void onCompleted() { }
          @Override public void onError(Throwable e) {
            Log.d(TAG, "Library initialization failed.", e);
            Toast.makeText(SplashActivity.this, R.string.error_fatal, Toast.LENGTH_SHORT).show();
          }
          //@formatter:on
          @Override public void onNext(SplashLibrary splashLibrary) {
            final String initialized = splashLibrary.initializedString();
            Toast.makeText(SplashActivity.this, initialized, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra(MainActivity.EXTRA_USEFUL_STRING_KEY, splashLibrary.usefulString());
            startActivity(intent);

            finish();
          }
        });
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

    // Optional: release the resources which were acquired in {@link SplashModule}.
    FastStartupApp.app(this).releaseSplashComponent();
  }
}
