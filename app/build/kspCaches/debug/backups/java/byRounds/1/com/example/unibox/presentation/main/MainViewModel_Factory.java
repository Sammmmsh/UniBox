package com.example.unibox.presentation.main;

import com.example.unibox.data.workers.MetadataWorkScheduler;
import com.example.unibox.domain.repository.UniBoxRepository;
import com.example.unibox.domain.usecase.DeleteItemUseCase;
import com.example.unibox.domain.usecase.GetItemsUseCase;
import com.example.unibox.domain.usecase.SaveItemUseCase;
import com.example.unibox.domain.usecase.SearchItemsUseCase;
import com.example.unibox.util.ConnectivityObserver;
import com.example.unibox.util.SmartReviewManager;
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<GetItemsUseCase> getItemsUseCaseProvider;

  private final Provider<SearchItemsUseCase> searchItemsUseCaseProvider;

  private final Provider<SaveItemUseCase> saveItemUseCaseProvider;

  private final Provider<DeleteItemUseCase> deleteItemUseCaseProvider;

  private final Provider<UniBoxRepository> repositoryProvider;

  private final Provider<MetadataWorkScheduler> metadataWorkSchedulerProvider;

  private final Provider<ConnectivityObserver> connectivityObserverProvider;

  private final Provider<SmartReviewManager> smartReviewManagerProvider;

  public MainViewModel_Factory(Provider<GetItemsUseCase> getItemsUseCaseProvider,
      Provider<SearchItemsUseCase> searchItemsUseCaseProvider,
      Provider<SaveItemUseCase> saveItemUseCaseProvider,
      Provider<DeleteItemUseCase> deleteItemUseCaseProvider,
      Provider<UniBoxRepository> repositoryProvider,
      Provider<MetadataWorkScheduler> metadataWorkSchedulerProvider,
      Provider<ConnectivityObserver> connectivityObserverProvider,
      Provider<SmartReviewManager> smartReviewManagerProvider) {
    this.getItemsUseCaseProvider = getItemsUseCaseProvider;
    this.searchItemsUseCaseProvider = searchItemsUseCaseProvider;
    this.saveItemUseCaseProvider = saveItemUseCaseProvider;
    this.deleteItemUseCaseProvider = deleteItemUseCaseProvider;
    this.repositoryProvider = repositoryProvider;
    this.metadataWorkSchedulerProvider = metadataWorkSchedulerProvider;
    this.connectivityObserverProvider = connectivityObserverProvider;
    this.smartReviewManagerProvider = smartReviewManagerProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(getItemsUseCaseProvider.get(), searchItemsUseCaseProvider.get(), saveItemUseCaseProvider.get(), deleteItemUseCaseProvider.get(), repositoryProvider.get(), metadataWorkSchedulerProvider.get(), connectivityObserverProvider.get(), smartReviewManagerProvider.get());
  }

  public static MainViewModel_Factory create(Provider<GetItemsUseCase> getItemsUseCaseProvider,
      Provider<SearchItemsUseCase> searchItemsUseCaseProvider,
      Provider<SaveItemUseCase> saveItemUseCaseProvider,
      Provider<DeleteItemUseCase> deleteItemUseCaseProvider,
      Provider<UniBoxRepository> repositoryProvider,
      Provider<MetadataWorkScheduler> metadataWorkSchedulerProvider,
      Provider<ConnectivityObserver> connectivityObserverProvider,
      Provider<SmartReviewManager> smartReviewManagerProvider) {
    return new MainViewModel_Factory(getItemsUseCaseProvider, searchItemsUseCaseProvider, saveItemUseCaseProvider, deleteItemUseCaseProvider, repositoryProvider, metadataWorkSchedulerProvider, connectivityObserverProvider, smartReviewManagerProvider);
  }

  public static MainViewModel newInstance(GetItemsUseCase getItemsUseCase,
      SearchItemsUseCase searchItemsUseCase, SaveItemUseCase saveItemUseCase,
      DeleteItemUseCase deleteItemUseCase, UniBoxRepository repository,
      MetadataWorkScheduler metadataWorkScheduler, ConnectivityObserver connectivityObserver,
      SmartReviewManager smartReviewManager) {
    return new MainViewModel(getItemsUseCase, searchItemsUseCase, saveItemUseCase, deleteItemUseCase, repository, metadataWorkScheduler, connectivityObserver, smartReviewManager);
  }
}
