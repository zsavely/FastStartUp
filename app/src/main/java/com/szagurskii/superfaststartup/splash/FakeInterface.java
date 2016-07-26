package com.szagurskii.superfaststartup.splash;

import retrofit2.http.GET;
import rx.Observable;

/**
 * @author Savelii Zagurskii
 */
public interface FakeInterface {
  @GET("posts/1") Observable<String> firstPost();
}
