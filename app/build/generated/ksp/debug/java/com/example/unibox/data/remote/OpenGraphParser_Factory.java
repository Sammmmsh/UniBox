package com.example.unibox.data.remote;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class OpenGraphParser_Factory implements Factory<OpenGraphParser> {
  private final Provider<OkHttpClient> httpClientProvider;

  private final Provider<PublicWebUrlValidator> urlValidatorProvider;

  public OpenGraphParser_Factory(Provider<OkHttpClient> httpClientProvider,
      Provider<PublicWebUrlValidator> urlValidatorProvider) {
    this.httpClientProvider = httpClientProvider;
    this.urlValidatorProvider = urlValidatorProvider;
  }

  @Override
  public OpenGraphParser get() {
    return newInstance(httpClientProvider.get(), urlValidatorProvider.get());
  }

  public static OpenGraphParser_Factory create(Provider<OkHttpClient> httpClientProvider,
      Provider<PublicWebUrlValidator> urlValidatorProvider) {
    return new OpenGraphParser_Factory(httpClientProvider, urlValidatorProvider);
  }

  public static OpenGraphParser newInstance(OkHttpClient httpClient,
      PublicWebUrlValidator urlValidator) {
    return new OpenGraphParser(httpClient, urlValidator);
  }
}
