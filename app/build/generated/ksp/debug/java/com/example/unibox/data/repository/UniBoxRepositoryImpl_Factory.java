package com.example.unibox.data.repository;

import com.example.unibox.data.local.UniBoxItemDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class UniBoxRepositoryImpl_Factory implements Factory<UniBoxRepositoryImpl> {
  private final Provider<UniBoxItemDao> daoProvider;

  public UniBoxRepositoryImpl_Factory(Provider<UniBoxItemDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public UniBoxRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static UniBoxRepositoryImpl_Factory create(Provider<UniBoxItemDao> daoProvider) {
    return new UniBoxRepositoryImpl_Factory(daoProvider);
  }

  public static UniBoxRepositoryImpl newInstance(UniBoxItemDao dao) {
    return new UniBoxRepositoryImpl(dao);
  }
}
