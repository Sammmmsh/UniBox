package com.example.unibox.data.remote;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class OpenGraphParser_Factory implements Factory<OpenGraphParser> {
  @Override
  public OpenGraphParser get() {
    return newInstance();
  }

  public static OpenGraphParser_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static OpenGraphParser newInstance() {
    return new OpenGraphParser();
  }

  private static final class InstanceHolder {
    private static final OpenGraphParser_Factory INSTANCE = new OpenGraphParser_Factory();
  }
}
