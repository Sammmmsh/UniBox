package com.example.unibox.domain.usecase;

import com.example.unibox.domain.organization.OrganizationEngine;
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
public final class SaveItemUseCase_Factory implements Factory<SaveItemUseCase> {
  private final Provider<UniBoxRepository> repositoryProvider;

  private final Provider<OrganizationEngine> organizationEngineProvider;

  public SaveItemUseCase_Factory(Provider<UniBoxRepository> repositoryProvider,
      Provider<OrganizationEngine> organizationEngineProvider) {
    this.repositoryProvider = repositoryProvider;
    this.organizationEngineProvider = organizationEngineProvider;
  }

  @Override
  public SaveItemUseCase get() {
    return newInstance(repositoryProvider.get(), organizationEngineProvider.get());
  }

  public static SaveItemUseCase_Factory create(Provider<UniBoxRepository> repositoryProvider,
      Provider<OrganizationEngine> organizationEngineProvider) {
    return new SaveItemUseCase_Factory(repositoryProvider, organizationEngineProvider);
  }

  public static SaveItemUseCase newInstance(UniBoxRepository repository,
      OrganizationEngine organizationEngine) {
    return new SaveItemUseCase(repository, organizationEngine);
  }
}
