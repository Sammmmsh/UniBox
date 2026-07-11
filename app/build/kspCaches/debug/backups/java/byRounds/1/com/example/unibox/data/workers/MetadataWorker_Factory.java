package com.example.unibox.data.workers;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.example.unibox.data.local.UniBoxItemDao;
import com.example.unibox.data.remote.OpenGraphParser;
import dagger.internal.DaggerGenerated;
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
public final class MetadataWorker_Factory {
  private final Provider<UniBoxItemDao> daoProvider;

  private final Provider<OpenGraphParser> openGraphParserProvider;

  public MetadataWorker_Factory(Provider<UniBoxItemDao> daoProvider,
      Provider<OpenGraphParser> openGraphParserProvider) {
    this.daoProvider = daoProvider;
    this.openGraphParserProvider = openGraphParserProvider;
  }

  public MetadataWorker get(Context appContext, WorkerParameters workerParams) {
    return newInstance(appContext, workerParams, daoProvider.get(), openGraphParserProvider.get());
  }

  public static MetadataWorker_Factory create(Provider<UniBoxItemDao> daoProvider,
      Provider<OpenGraphParser> openGraphParserProvider) {
    return new MetadataWorker_Factory(daoProvider, openGraphParserProvider);
  }

  public static MetadataWorker newInstance(Context appContext, WorkerParameters workerParams,
      UniBoxItemDao dao, OpenGraphParser openGraphParser) {
    return new MetadataWorker(appContext, workerParams, dao, openGraphParser);
  }
}
