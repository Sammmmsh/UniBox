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
public final class GetItemsUseCase_Factory implements Factory<GetItemsUseCase> {
  private final Provider<UniBoxRepository> repositoryProvider;

  public GetItemsUseCase_Factory(Provider<UniBoxRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetItemsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetItemsUseCase_Factory create(Provider<UniBoxRepository> repositoryProvider) {
    return new GetItemsUseCase_Factory(repositoryProvider);
  }

  public static GetItemsUseCase newInstance(UniBoxRepository repository) {
    return new GetItemsUseCase(repository);
  }
}
