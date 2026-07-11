package com.example.unibox.di;

import com.example.unibox.data.local.UniBoxDatabase;
import com.example.unibox.data.local.UniBoxItemDao;
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
public final class AppModule_ProvideUniBoxItemDaoFactory implements Factory<UniBoxItemDao> {
  private final Provider<UniBoxDatabase> databaseProvider;

  public AppModule_ProvideUniBoxItemDaoFactory(Provider<UniBoxDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public UniBoxItemDao get() {
    return provideUniBoxItemDao(databaseProvider.get());
  }

  public static AppModule_ProvideUniBoxItemDaoFactory create(
      Provider<UniBoxDatabase> databaseProvider) {
    return new AppModule_ProvideUniBoxItemDaoFactory(databaseProvider);
  }

  public static UniBoxItemDao provideUniBoxItemDao(UniBoxDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideUniBoxItemDao(database));
  }
}
