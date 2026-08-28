package com.example.unibox.presentation.settings;

import com.example.unibox.data.export.LibraryExporter;
import com.example.unibox.domain.repository.ThemePreferences;
import com.example.unibox.domain.repository.UniBoxRepository;
import com.example.unibox.domain.repository.WebPreviewPreferences;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<UniBoxRepository> repositoryProvider;

  private final Provider<LibraryExporter> libraryExporterProvider;

  private final Provider<ThemePreferences> themePreferencesProvider;

  private final Provider<WebPreviewPreferences> webPreviewPreferencesProvider;

  public SettingsViewModel_Factory(Provider<UniBoxRepository> repositoryProvider,
      Provider<LibraryExporter> libraryExporterProvider,
      Provider<ThemePreferences> themePreferencesProvider,
      Provider<WebPreviewPreferences> webPreviewPreferencesProvider) {
    this.repositoryProvider = repositoryProvider;
    this.libraryExporterProvider = libraryExporterProvider;
    this.themePreferencesProvider = themePreferencesProvider;
    this.webPreviewPreferencesProvider = webPreviewPreferencesProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(repositoryProvider.get(), libraryExporterProvider.get(), themePreferencesProvider.get(), webPreviewPreferencesProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<UniBoxRepository> repositoryProvider,
      Provider<LibraryExporter> libraryExporterProvider,
      Provider<ThemePreferences> themePreferencesProvider,
      Provider<WebPreviewPreferences> webPreviewPreferencesProvider) {
    return new SettingsViewModel_Factory(repositoryProvider, libraryExporterProvider, themePreferencesProvider, webPreviewPreferencesProvider);
  }

  public static SettingsViewModel newInstance(UniBoxRepository repository,
      LibraryExporter libraryExporter, ThemePreferences themePreferences,
      WebPreviewPreferences webPreviewPreferences) {
    return new SettingsViewModel(repository, libraryExporter, themePreferences, webPreviewPreferences);
  }
}
