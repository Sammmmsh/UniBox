package com.example.unibox.util;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class SmartReviewManager_Factory implements Factory<SmartReviewManager> {
  private final Provider<Context> contextProvider;

  public SmartReviewManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SmartReviewManager get() {
    return newInstance(contextProvider.get());
  }

  public static SmartReviewManager_Factory create(Provider<Context> contextProvider) {
    return new SmartReviewManager_Factory(contextProvider);
  }

  public static SmartReviewManager newInstance(Context context) {
    return new SmartReviewManager(context);
  }
}
