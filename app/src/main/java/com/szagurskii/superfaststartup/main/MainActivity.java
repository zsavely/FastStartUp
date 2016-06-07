package com.szagurskii.superfaststartup.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.szagurskii.superfaststartup.R;
import com.szagurskii.superfaststartup.SuperFastStartupApp;
import com.szagurskii.superfaststartup.util.VeryHeavyLibrary;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

  @Inject SuperFastStartupApp application;
  @Inject Observable<VeryHeavyLibrary> veryHeavyLibraryObservable;

  private Subscription subscription;
  private VeryHeavyLibrary veryHeavyLibrary;
  private TextView textView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    textView = (TextView) findViewById(R.id.textview);

    SuperFastStartupApp.app(this).mainComponent().inject(this);

    Preconditions.checkNotNull(application);
    Preconditions.checkNotNull(veryHeavyLibraryObservable);

    subscription = veryHeavyLibraryObservable
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.computation())
        .subscribe(new Action1<VeryHeavyLibrary>() {
          @Override public void call(VeryHeavyLibrary veryHeavyLibrary) {
            MainActivity.this.veryHeavyLibrary = veryHeavyLibrary;

            // Won't be null, because we unsubscribe in onPause().
            textView.append("\n" + veryHeavyLibrary.initializedString());

            Toast.makeText(MainActivity.this, veryHeavyLibrary.initializedString(), Toast.LENGTH_SHORT).show();
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
