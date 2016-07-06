package com.szagurskii.superfaststartup.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.szagurskii.superfaststartup.R;

public final class MainActivity extends AppCompatActivity {
  public static final String EXTRA_USEFUL_STRING = "extra_useful_string";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
}
