package com.example.unibox.data.repository;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ThemePreferencesImpl_Factory implements Factory<ThemePreferencesImpl> {
  private final Provider<Context> contextProvider;

  public ThemePreferencesImpl_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ThemePreferencesImpl get() {
    return newInstance(contextProvider.get());
  }

  public static ThemePreferencesImpl_Factory create(Provider<Context> contextProvider) {
    return new ThemePreferencesImpl_Factory(contextProvider);
  }

  public static ThemePreferencesImpl newInstance(Context context) {
    return new ThemePreferencesImpl(context);
  }
}
