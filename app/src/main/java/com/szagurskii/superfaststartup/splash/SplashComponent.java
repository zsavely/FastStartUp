package com.szagurskii.superfaststartup.splash;

import dagger.Component;

/**
 * @author Savelii Zagurskii
 */
@Component(modules = {
    SplashModule.class,
})
@SplashScope
public interface SplashComponent {
  void inject(SplashActivity splashActivity);
}
