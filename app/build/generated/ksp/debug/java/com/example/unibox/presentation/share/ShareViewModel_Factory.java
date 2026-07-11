package com.example.unibox.presentation.share;

import android.content.Context;
import com.example.unibox.domain.usecase.SaveItemUseCase;
import com.example.unibox.ml.TextExtractor;
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
public final class ShareViewModel_Factory implements Factory<ShareViewModel> {
  private final Provider<SaveItemUseCase> saveItemUseCaseProvider;

  private final Provider<TextExtractor> textExtractorProvider;

  private final Provider<Context> appContextProvider;

  public ShareViewModel_Factory(Provider<SaveItemUseCase> saveItemUseCaseProvider,
      Provider<TextExtractor> textExtractorProvider, Provider<Context> appContextProvider) {
    this.saveItemUseCaseProvider = saveItemUseCaseProvider;
    this.textExtractorProvider = textExtractorProvider;
    this.appContextProvider = appContextProvider;
  }

  @Override
  public ShareViewModel get() {
    return newInstance(saveItemUseCaseProvider.get(), textExtractorProvider.get(), appContextProvider.get());
  }

  public static ShareViewModel_Factory create(Provider<SaveItemUseCase> saveItemUseCaseProvider,
      Provider<TextExtractor> textExtractorProvider, Provider<Context> appContextProvider) {
    return new ShareViewModel_Factory(saveItemUseCaseProvider, textExtractorProvider, appContextProvider);
  }

  public static ShareViewModel newInstance(SaveItemUseCase saveItemUseCase,
      TextExtractor textExtractor, Context appContext) {
    return new ShareViewModel(saveItemUseCase, textExtractor, appContext);
  }
}
