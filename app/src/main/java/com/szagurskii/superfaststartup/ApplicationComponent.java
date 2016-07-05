package com.szagurskii.superfaststartup;

import java.util.Random;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    ApplicationModule.class,
})
public interface ApplicationComponent {
  /**
   * {@link Random} instance from {@link ApplicationModule}
   * which now can be injected to children
   * that depend on {@link ApplicationComponent}.
   */
  Random random();
}
