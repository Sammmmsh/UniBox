package com.example.unibox.presentation.settings;

import android.content.Context;
import com.example.unibox.domain.repository.UniBoxRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<UniBoxRepository> repositoryProvider;

  private final Provider<Context> contextProvider;

  public SettingsViewModel_Factory(Provider<UniBoxRepository> repositoryProvider,
      Provider<Context> contextProvider) {
    this.repositoryProvider = repositoryProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(repositoryProvider.get(), contextProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<UniBoxRepository> repositoryProvider,
      Provider<Context> contextProvider) {
    return new SettingsViewModel_Factory(repositoryProvider, contextProvider);
  }

  public static SettingsViewModel newInstance(UniBoxRepository repository, Context context) {
    return new SettingsViewModel(repository, context);
  }
}
