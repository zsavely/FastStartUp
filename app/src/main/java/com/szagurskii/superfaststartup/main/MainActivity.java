package com.szagurskii.superfaststartup.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.szagurskii.superfaststartup.FastStartupApp;
import com.szagurskii.superfaststartup.R;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public final class MainActivity extends AppCompatActivity {
  public static final String EXTRA_USEFUL_STRING = "extra_useful_string";

  /**
   * Observable which will emit an item when fully initialized.
   * {@link rx.Single} can also be used.
   */
  @Inject Observable<MainLibrary> mainLibraryObservable;

  /** Subscription to unsubscribe in onStop(). */
  private Subscription subscription;

  /** Library which will be initialized in the background. */
  private MainLibrary mainLibrary;

  /** Simple TextView. */
  private TextView textView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initialize views.
    setContentView(R.layout.activity_main);
    textView = (TextView) findViewById(R.id.textview);

    // Inject {@code mainLibraryObservable}.
    FastStartupApp.app(this).mainComponent().inject(this);

    // Check that we successfully injected the {@code mainLibraryObservable}.
    Preconditions.checkNotNull(mainLibraryObservable);

    // Create subscription.
    subscription = mainLibraryObservable
        // Init library on another thread.
        .subscribeOn(Schedulers.computation())
        // Get the library on main thread.
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<MainLibrary>() {
          @Override public void call(MainLibrary mainLibrary) {
            MainActivity.this.mainLibrary = mainLibrary;

            // Won't be null, because we unsubscribe in onStop().
            textView.append(mainLibrary.initializedString() + "\n");
          }
        });
  }

  @Override protected void onStop() {
    super.onStop();

    // Unsubscribe to release resources.
    if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();

    // Optional: release the resources which were acquired in {@link MainModule}.
    FastStartupApp.app(this).releaseMainComponent();

    // Release View references.
    textView = null;
  }
}
