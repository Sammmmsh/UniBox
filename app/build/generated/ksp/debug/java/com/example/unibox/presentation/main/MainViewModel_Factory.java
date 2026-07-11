package com.example.unibox.presentation.main;

import android.content.Context;
import com.example.unibox.domain.usecase.DeleteItemUseCase;
import com.example.unibox.domain.usecase.GetItemsUseCase;
import com.example.unibox.domain.usecase.SaveItemUseCase;
import com.example.unibox.domain.usecase.SearchItemsUseCase;
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<GetItemsUseCase> getItemsUseCaseProvider;

  private final Provider<SearchItemsUseCase> searchItemsUseCaseProvider;

  private final Provider<SaveItemUseCase> saveItemUseCaseProvider;

  private final Provider<DeleteItemUseCase> deleteItemUseCaseProvider;

  private final Provider<Context> appContextProvider;

  public MainViewModel_Factory(Provider<GetItemsUseCase> getItemsUseCaseProvider,
      Provider<SearchItemsUseCase> searchItemsUseCaseProvider,
      Provider<SaveItemUseCase> saveItemUseCaseProvider,
      Provider<DeleteItemUseCase> deleteItemUseCaseProvider, Provider<Context> appContextProvider) {
    this.getItemsUseCaseProvider = getItemsUseCaseProvider;
    this.searchItemsUseCaseProvider = searchItemsUseCaseProvider;
    this.saveItemUseCaseProvider = saveItemUseCaseProvider;
    this.deleteItemUseCaseProvider = deleteItemUseCaseProvider;
    this.appContextProvider = appContextProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(getItemsUseCaseProvider.get(), searchItemsUseCaseProvider.get(), saveItemUseCaseProvider.get(), deleteItemUseCaseProvider.get(), appContextProvider.get());
  }

  public static MainViewModel_Factory create(Provider<GetItemsUseCase> getItemsUseCaseProvider,
      Provider<SearchItemsUseCase> searchItemsUseCaseProvider,
      Provider<SaveItemUseCase> saveItemUseCaseProvider,
      Provider<DeleteItemUseCase> deleteItemUseCaseProvider, Provider<Context> appContextProvider) {
    return new MainViewModel_Factory(getItemsUseCaseProvider, searchItemsUseCaseProvider, saveItemUseCaseProvider, deleteItemUseCaseProvider, appContextProvider);
  }

  public static MainViewModel newInstance(GetItemsUseCase getItemsUseCase,
      SearchItemsUseCase searchItemsUseCase, SaveItemUseCase saveItemUseCase,
      DeleteItemUseCase deleteItemUseCase, Context appContext) {
    return new MainViewModel(getItemsUseCase, searchItemsUseCase, saveItemUseCase, deleteItemUseCase, appContext);
  }
}
