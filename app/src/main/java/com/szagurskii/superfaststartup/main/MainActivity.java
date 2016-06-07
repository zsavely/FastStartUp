package com.szagurskii.superfaststartup.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.szagurskii.superfaststartup.R;
import com.szagurskii.superfaststartup.SuperFastStartupApp;
import com.szagurskii.superfaststartup.util.HeavyLibrary;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

  @Inject SuperFastStartupApp application;
  @Inject Observable<HeavyLibrary> heavy;

  private Subscription subscription;
  private HeavyLibrary heavyLibrary;
  private TextView textView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    textView = (TextView) findViewById(R.id.textview);

    SuperFastStartupApp.app(this).mainComponent().inject(this);

    Preconditions.checkNotNull(application);
    Preconditions.checkNotNull(heavy);

    subscription = heavy
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.computation())
        .subscribe(new Action1<HeavyLibrary>() {
          @Override public void call(HeavyLibrary heavyLibrary) {
            MainActivity.this.heavyLibrary = heavyLibrary;

            // Won't be null, because we unsubscribe in onPause().
            textView.append("\n" + heavyLibrary.initializedString());

            Toast.makeText(MainActivity.this, heavyLibrary.initializedString(), Toast.LENGTH_SHORT).show();
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

    application.releaseMainComponent();
    textView = null;
  }
}
