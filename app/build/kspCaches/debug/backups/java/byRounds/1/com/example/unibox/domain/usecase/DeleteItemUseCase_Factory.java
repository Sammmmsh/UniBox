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
public final class DeleteItemUseCase_Factory implements Factory<DeleteItemUseCase> {
  private final Provider<UniBoxRepository> repositoryProvider;

  public DeleteItemUseCase_Factory(Provider<UniBoxRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DeleteItemUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static DeleteItemUseCase_Factory create(Provider<UniBoxRepository> repositoryProvider) {
    return new DeleteItemUseCase_Factory(repositoryProvider);
  }

  public static DeleteItemUseCase newInstance(UniBoxRepository repository) {
    return new DeleteItemUseCase(repository);
  }
}
