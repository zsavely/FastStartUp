package com.szagurskii.superfaststartup.splash;

import com.szagurskii.superfaststartup.ApplicationComponent;

import dagger.Component;

/**
 * @author Savelii Zagurskii
 */
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
