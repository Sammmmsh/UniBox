package com.example.unibox.di;

import android.content.Context;
import com.example.unibox.domain.repository.ThemePreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppModule_ProvideThemePreferencesFactory implements Factory<ThemePreferences> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideThemePreferencesFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ThemePreferences get() {
    return provideThemePreferences(contextProvider.get());
  }

  public static AppModule_ProvideThemePreferencesFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideThemePreferencesFactory(contextProvider);
  }

  public static ThemePreferences provideThemePreferences(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideThemePreferences(context));
  }
}
