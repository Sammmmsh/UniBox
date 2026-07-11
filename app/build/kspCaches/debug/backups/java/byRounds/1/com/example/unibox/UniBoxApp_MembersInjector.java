package com.example.unibox;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class UniBoxApp_MembersInjector implements MembersInjector<UniBoxApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public UniBoxApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<UniBoxApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new UniBoxApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(UniBoxApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.example.unibox.UniBoxApp.workerFactory")
  public static void injectWorkerFactory(UniBoxApp instance, HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
