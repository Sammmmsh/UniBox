package com.example.unibox;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.example.unibox.data.local.UniBoxDatabase;
import com.example.unibox.data.local.UniBoxItemDao;
import com.example.unibox.data.remote.OpenGraphParser;
import com.example.unibox.data.workers.MetadataWorker;
import com.example.unibox.data.workers.MetadataWorker_AssistedFactory;
import com.example.unibox.di.AppModule_ProvideDatabaseFactory;
import com.example.unibox.di.AppModule_ProvideThemePreferencesFactory;
import com.example.unibox.di.AppModule_ProvideUniBoxItemDaoFactory;
import com.example.unibox.di.AppModule_ProvideUniBoxRepositoryFactory;
import com.example.unibox.domain.repository.ThemePreferences;
import com.example.unibox.domain.repository.UniBoxRepository;
import com.example.unibox.domain.usecase.DeleteItemUseCase;
import com.example.unibox.domain.usecase.GetItemsUseCase;
import com.example.unibox.domain.usecase.SaveItemUseCase;
import com.example.unibox.domain.usecase.SearchItemsUseCase;
import com.example.unibox.location.GeofenceManager;
import com.example.unibox.ml.TextExtractor;
import com.example.unibox.presentation.detail.DetailViewModel;
import com.example.unibox.presentation.detail.DetailViewModel_HiltModules;
import com.example.unibox.presentation.main.MainViewModel;
import com.example.unibox.presentation.main.MainViewModel_HiltModules;
import com.example.unibox.presentation.settings.SettingsViewModel;
import com.example.unibox.presentation.settings.SettingsViewModel_HiltModules;
import com.example.unibox.presentation.share.ShareReceiverActivity;
import com.example.unibox.presentation.share.ShareViewModel;
import com.example.unibox.presentation.share.ShareViewModel_HiltModules;
import com.example.unibox.util.ConnectivityObserver;
import com.example.unibox.util.SmartReviewManager;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SingleCheck;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerUniBoxApp_HiltComponents_SingletonC {
  private DaggerUniBoxApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public UniBoxApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements UniBoxApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public UniBoxApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements UniBoxApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public UniBoxApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements UniBoxApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public UniBoxApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements UniBoxApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public UniBoxApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements UniBoxApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public UniBoxApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements UniBoxApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public UniBoxApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements UniBoxApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public UniBoxApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends UniBoxApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends UniBoxApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends UniBoxApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends UniBoxApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public void injectShareReceiverActivity(ShareReceiverActivity shareReceiverActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(4).put(LazyClassKeyProvider.com_example_unibox_presentation_detail_DetailViewModel, DetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_unibox_presentation_main_MainViewModel, MainViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_unibox_presentation_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_unibox_presentation_share_ShareViewModel, ShareViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectThemePreferences(instance, singletonCImpl.provideThemePreferencesProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_example_unibox_presentation_detail_DetailViewModel = "com.example.unibox.presentation.detail.DetailViewModel";

      static String com_example_unibox_presentation_main_MainViewModel = "com.example.unibox.presentation.main.MainViewModel";

      static String com_example_unibox_presentation_settings_SettingsViewModel = "com.example.unibox.presentation.settings.SettingsViewModel";

      static String com_example_unibox_presentation_share_ShareViewModel = "com.example.unibox.presentation.share.ShareViewModel";

      @KeepFieldType
      DetailViewModel com_example_unibox_presentation_detail_DetailViewModel2;

      @KeepFieldType
      MainViewModel com_example_unibox_presentation_main_MainViewModel2;

      @KeepFieldType
      SettingsViewModel com_example_unibox_presentation_settings_SettingsViewModel2;

      @KeepFieldType
      ShareViewModel com_example_unibox_presentation_share_ShareViewModel2;
    }
  }

  private static final class ViewModelCImpl extends UniBoxApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<DetailViewModel> detailViewModelProvider;

    private Provider<MainViewModel> mainViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<ShareViewModel> shareViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private GetItemsUseCase getItemsUseCase() {
      return new GetItemsUseCase(singletonCImpl.provideUniBoxRepositoryProvider.get());
    }

    private SearchItemsUseCase searchItemsUseCase() {
      return new SearchItemsUseCase(singletonCImpl.provideUniBoxRepositoryProvider.get());
    }

    private SaveItemUseCase saveItemUseCase() {
      return new SaveItemUseCase(singletonCImpl.provideUniBoxRepositoryProvider.get());
    }

    private DeleteItemUseCase deleteItemUseCase() {
      return new DeleteItemUseCase(singletonCImpl.provideUniBoxRepositoryProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.detailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.mainViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.shareViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(4).put(LazyClassKeyProvider.com_example_unibox_presentation_detail_DetailViewModel, ((Provider) detailViewModelProvider)).put(LazyClassKeyProvider.com_example_unibox_presentation_main_MainViewModel, ((Provider) mainViewModelProvider)).put(LazyClassKeyProvider.com_example_unibox_presentation_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).put(LazyClassKeyProvider.com_example_unibox_presentation_share_ShareViewModel, ((Provider) shareViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_example_unibox_presentation_main_MainViewModel = "com.example.unibox.presentation.main.MainViewModel";

      static String com_example_unibox_presentation_settings_SettingsViewModel = "com.example.unibox.presentation.settings.SettingsViewModel";

      static String com_example_unibox_presentation_share_ShareViewModel = "com.example.unibox.presentation.share.ShareViewModel";

      static String com_example_unibox_presentation_detail_DetailViewModel = "com.example.unibox.presentation.detail.DetailViewModel";

      @KeepFieldType
      MainViewModel com_example_unibox_presentation_main_MainViewModel2;

      @KeepFieldType
      SettingsViewModel com_example_unibox_presentation_settings_SettingsViewModel2;

      @KeepFieldType
      ShareViewModel com_example_unibox_presentation_share_ShareViewModel2;

      @KeepFieldType
      DetailViewModel com_example_unibox_presentation_detail_DetailViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.example.unibox.presentation.detail.DetailViewModel 
          return (T) new DetailViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.provideUniBoxRepositoryProvider.get(), singletonCImpl.geofenceManagerProvider.get());

          case 1: // com.example.unibox.presentation.main.MainViewModel 
          return (T) new MainViewModel(viewModelCImpl.getItemsUseCase(), viewModelCImpl.searchItemsUseCase(), viewModelCImpl.saveItemUseCase(), viewModelCImpl.deleteItemUseCase(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.connectivityObserverProvider.get(), singletonCImpl.smartReviewManagerProvider.get());

          case 2: // com.example.unibox.presentation.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.provideUniBoxRepositoryProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideThemePreferencesProvider.get());

          case 3: // com.example.unibox.presentation.share.ShareViewModel 
          return (T) new ShareViewModel(viewModelCImpl.saveItemUseCase(), singletonCImpl.textExtractorProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends UniBoxApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends UniBoxApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends UniBoxApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<UniBoxDatabase> provideDatabaseProvider;

    private Provider<UniBoxItemDao> provideUniBoxItemDaoProvider;

    private Provider<OpenGraphParser> openGraphParserProvider;

    private Provider<MetadataWorker_AssistedFactory> metadataWorker_AssistedFactoryProvider;

    private Provider<ThemePreferences> provideThemePreferencesProvider;

    private Provider<UniBoxRepository> provideUniBoxRepositoryProvider;

    private Provider<GeofenceManager> geofenceManagerProvider;

    private Provider<ConnectivityObserver> connectivityObserverProvider;

    private Provider<SmartReviewManager> smartReviewManagerProvider;

    private Provider<TextExtractor> textExtractorProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private Map<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return Collections.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>singletonMap("com.example.unibox.data.workers.MetadataWorker", ((Provider) metadataWorker_AssistedFactoryProvider));
    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<UniBoxDatabase>(singletonCImpl, 2));
      this.provideUniBoxItemDaoProvider = DoubleCheck.provider(new SwitchingProvider<UniBoxItemDao>(singletonCImpl, 1));
      this.openGraphParserProvider = DoubleCheck.provider(new SwitchingProvider<OpenGraphParser>(singletonCImpl, 3));
      this.metadataWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<MetadataWorker_AssistedFactory>(singletonCImpl, 0));
      this.provideThemePreferencesProvider = DoubleCheck.provider(new SwitchingProvider<ThemePreferences>(singletonCImpl, 4));
      this.provideUniBoxRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<UniBoxRepository>(singletonCImpl, 5));
      this.geofenceManagerProvider = DoubleCheck.provider(new SwitchingProvider<GeofenceManager>(singletonCImpl, 6));
      this.connectivityObserverProvider = DoubleCheck.provider(new SwitchingProvider<ConnectivityObserver>(singletonCImpl, 7));
      this.smartReviewManagerProvider = DoubleCheck.provider(new SwitchingProvider<SmartReviewManager>(singletonCImpl, 8));
      this.textExtractorProvider = DoubleCheck.provider(new SwitchingProvider<TextExtractor>(singletonCImpl, 9));
    }

    @Override
    public void injectUniBoxApp(UniBoxApp uniBoxApp) {
      injectUniBoxApp2(uniBoxApp);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private UniBoxApp injectUniBoxApp2(UniBoxApp instance) {
      UniBoxApp_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.example.unibox.data.workers.MetadataWorker_AssistedFactory 
          return (T) new MetadataWorker_AssistedFactory() {
            @Override
            public MetadataWorker create(Context appContext, WorkerParameters workerParams) {
              return new MetadataWorker(appContext, workerParams, singletonCImpl.provideUniBoxItemDaoProvider.get(), singletonCImpl.openGraphParserProvider.get());
            }
          };

          case 1: // com.example.unibox.data.local.UniBoxItemDao 
          return (T) AppModule_ProvideUniBoxItemDaoFactory.provideUniBoxItemDao(singletonCImpl.provideDatabaseProvider.get());

          case 2: // com.example.unibox.data.local.UniBoxDatabase 
          return (T) AppModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.example.unibox.data.remote.OpenGraphParser 
          return (T) new OpenGraphParser();

          case 4: // com.example.unibox.domain.repository.ThemePreferences 
          return (T) AppModule_ProvideThemePreferencesFactory.provideThemePreferences(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 5: // com.example.unibox.domain.repository.UniBoxRepository 
          return (T) AppModule_ProvideUniBoxRepositoryFactory.provideUniBoxRepository(singletonCImpl.provideUniBoxItemDaoProvider.get());

          case 6: // com.example.unibox.location.GeofenceManager 
          return (T) new GeofenceManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 7: // com.example.unibox.util.ConnectivityObserver 
          return (T) new ConnectivityObserver(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 8: // com.example.unibox.util.SmartReviewManager 
          return (T) new SmartReviewManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 9: // com.example.unibox.ml.TextExtractor 
          return (T) new TextExtractor();

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
