package com.szagurskii.superfaststartup.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.szagurskii.superfaststartup.main.MainActivity;

public class SplashActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent i = new Intent(this, MainActivity.class);
    startActivity(i);
    finish();
  }
}
