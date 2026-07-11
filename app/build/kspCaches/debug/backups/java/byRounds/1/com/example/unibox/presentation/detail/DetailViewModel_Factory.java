package com.example.unibox.presentation.detail;

import androidx.lifecycle.SavedStateHandle;
import com.example.unibox.domain.repository.UniBoxRepository;
import com.example.unibox.location.GeofenceManager;
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
public final class DetailViewModel_Factory implements Factory<DetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<UniBoxRepository> repositoryProvider;

  private final Provider<GeofenceManager> geofenceManagerProvider;

  public DetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<UniBoxRepository> repositoryProvider,
      Provider<GeofenceManager> geofenceManagerProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.repositoryProvider = repositoryProvider;
    this.geofenceManagerProvider = geofenceManagerProvider;
  }

  @Override
  public DetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), repositoryProvider.get(), geofenceManagerProvider.get());
  }

  public static DetailViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<UniBoxRepository> repositoryProvider,
      Provider<GeofenceManager> geofenceManagerProvider) {
    return new DetailViewModel_Factory(savedStateHandleProvider, repositoryProvider, geofenceManagerProvider);
  }

  public static DetailViewModel newInstance(SavedStateHandle savedStateHandle,
      UniBoxRepository repository, GeofenceManager geofenceManager) {
    return new DetailViewModel(savedStateHandle, repository, geofenceManager);
  }
}
