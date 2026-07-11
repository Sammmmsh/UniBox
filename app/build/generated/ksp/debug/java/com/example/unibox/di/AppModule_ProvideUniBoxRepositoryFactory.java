package com.example.unibox.di;

import com.example.unibox.data.local.UniBoxItemDao;
import com.example.unibox.domain.repository.UniBoxRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class AppModule_ProvideUniBoxRepositoryFactory implements Factory<UniBoxRepository> {
  private final Provider<UniBoxItemDao> daoProvider;

  public AppModule_ProvideUniBoxRepositoryFactory(Provider<UniBoxItemDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public UniBoxRepository get() {
    return provideUniBoxRepository(daoProvider.get());
  }

  public static AppModule_ProvideUniBoxRepositoryFactory create(
      Provider<UniBoxItemDao> daoProvider) {
    return new AppModule_ProvideUniBoxRepositoryFactory(daoProvider);
  }

  public static UniBoxRepository provideUniBoxRepository(UniBoxItemDao dao) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideUniBoxRepository(dao));
  }
}
