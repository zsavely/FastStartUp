package com.szagurskii.superfaststartup.main;

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
        MainModule.class,
    }
)
@MainScope
public interface MainComponent {
  void inject(MainActivity mainActivity);
}
