package com.szagurskii.superfaststartup.splash;

import com.szagurskii.superfaststartup.ApplicationComponent;

import dagger.Component;

@Component(
    dependencies = {
        ApplicationComponent.class
    },
    modules = {
        SplashModule.class,
    }
)
@SplashScope
public interface SplashComponent {
  void inject(SplashActivity splashActivity);
}
