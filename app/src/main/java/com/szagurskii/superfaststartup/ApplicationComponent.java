package com.szagurskii.superfaststartup;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    ApplicationModule.class,
})
public interface ApplicationComponent {
  SuperFastStartupApp app();
}
