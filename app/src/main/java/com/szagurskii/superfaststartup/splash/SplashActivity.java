package com.szagurskii.superfaststartup.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.szagurskii.superfaststartup.SuperFastStartupApp;
import com.szagurskii.superfaststartup.main.MainActivity;
import com.szagurskii.superfaststartup.util.Heavy2Library;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SplashActivity extends Activity {
  private static final String TAG = "SplashActivity";

  @Inject SuperFastStartupApp application;
  @Inject Observable<Heavy2Library> heavy;

  private Subscription subscription;
  private Heavy2Library heavy2Library;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SuperFastStartupApp.app(this).splashComponent().inject(this);

    Preconditions.checkNotNull(application);
    Preconditions.checkNotNull(heavy);

    subscription = heavy
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.computation())
        .subscribe(new Action1<Heavy2Library>() {
          @Override public void call(Heavy2Library heavy2Library) {
            SplashActivity.this.heavy2Library = heavy2Library;

            Log.d(TAG, heavy2Library.initializedString());

            Toast.makeText(SplashActivity.this, heavy2Library.initializedString(), Toast.LENGTH_SHORT).show();

            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
          }
        });
  }

  @Override protected void onPause() {
    super.onPause();

    if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();

    application.releaseSplashComponent();
  }
}
