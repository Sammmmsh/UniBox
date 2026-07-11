package com.example.unibox.data.workers;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MetadataWorker_AssistedFactory_Impl implements MetadataWorker_AssistedFactory {
  private final MetadataWorker_Factory delegateFactory;

  MetadataWorker_AssistedFactory_Impl(MetadataWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public MetadataWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<MetadataWorker_AssistedFactory> create(
      MetadataWorker_Factory delegateFactory) {
    return InstanceFactory.create(new MetadataWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<MetadataWorker_AssistedFactory> createFactoryProvider(
      MetadataWorker_Factory delegateFactory) {
    return InstanceFactory.create(new MetadataWorker_AssistedFactory_Impl(delegateFactory));
  }
}
