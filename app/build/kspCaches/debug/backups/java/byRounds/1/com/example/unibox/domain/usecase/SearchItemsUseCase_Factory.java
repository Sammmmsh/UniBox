package com.example.unibox.domain.usecase;

import com.example.unibox.domain.repository.UniBoxRepository;
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
public final class SearchItemsUseCase_Factory implements Factory<SearchItemsUseCase> {
  private final Provider<UniBoxRepository> repositoryProvider;

  public SearchItemsUseCase_Factory(Provider<UniBoxRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SearchItemsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SearchItemsUseCase_Factory create(Provider<UniBoxRepository> repositoryProvider) {
    return new SearchItemsUseCase_Factory(repositoryProvider);
  }

  public static SearchItemsUseCase newInstance(UniBoxRepository repository) {
    return new SearchItemsUseCase(repository);
  }
}
