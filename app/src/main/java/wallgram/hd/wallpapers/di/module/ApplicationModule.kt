package wallgram.hd.wallpapers.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.GlobalScope
import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.PREFERENCES
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.domain.HandleDomainError
import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.core.data.PreferenceDataStore
import wallgram.hd.wallpapers.core.data.ProvideRetrofitBuilder
import wallgram.hd.wallpapers.data.billing.BillingDataSource
import wallgram.hd.wallpapers.data.remote.LanguageInterceptor

import wallgram.hd.wallpapers.data.remote.ResolutionInterceptor
import wallgram.hd.wallpapers.data.colors.ColorsDataSource
import wallgram.hd.wallpapers.data.colors.ColorsRepository
import wallgram.hd.wallpapers.data.billing.BillingRepository
import wallgram.hd.wallpapers.data.colors.ColorMapper
import wallgram.hd.wallpapers.data.filters.BaseFiltersRepository
import wallgram.hd.wallpapers.data.filters.FiltersCloud
import wallgram.hd.wallpapers.data.filters.FiltersCloudDataSource
import wallgram.hd.wallpapers.data.filters.ProvideFiltersService
import wallgram.hd.wallpapers.data.gallery.GalleryData
import wallgram.hd.wallpapers.data.gallery.WallpapersCache
import wallgram.hd.wallpapers.data.home.BaseHomeRepository
import wallgram.hd.wallpapers.data.settings.FileCacheSource
import wallgram.hd.wallpapers.domain.filters.CategoryDomain
import wallgram.hd.wallpapers.domain.filters.CategoriesDomain
import wallgram.hd.wallpapers.domain.filters.CategoriesInteractor
import wallgram.hd.wallpapers.domain.filters.CategoriesRepository
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.domain.home.HomeDomain
import wallgram.hd.wallpapers.domain.home.HomeInteractor
import wallgram.hd.wallpapers.domain.home.HomeRepository
import wallgram.hd.wallpapers.domain.home.HomesDomain
import wallgram.hd.wallpapers.presentation.colors.ColorsCommunication
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.settings.SettingsCommunication
import wallgram.hd.wallpapers.data.workers.DownloadManager
import wallgram.hd.wallpapers.presentation.colors.NavigateColor
import wallgram.hd.wallpapers.presentation.filters.NavigateCarousel
import wallgram.hd.wallpapers.presentation.filters.NavigateFilter

import wallgram.hd.wallpapers.util.localization.LocalizationApplicationDelegate

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideContext(app: Application): Context = app

    @Singleton
    @Provides
    fun provideDownloadManager(context: Context): DownloadManager = DownloadManager.Base(context)


    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providePreferenceDataStore(sharedPreferences: SharedPreferences): PreferenceDataStore = PreferenceDataStore.Base(sharedPreferences)

    @Singleton
    @Provides
    fun provideRequestManager(
        application: Application
    ): RequestManager {
        return Glide.with(application)
    }

    @Provides
    fun provideSettingsCommunication(): SettingsCommunication =
        SettingsCommunication.Base()


    @Provides
    @Singleton
    fun provideCache(context: Context): FileCacheSource = FileCacheSource.Base(context)

    @Provides
    @Singleton
    fun provideLocalizationDelegate(context: Context): LocalizationApplicationDelegate =
        LocalizationApplicationDelegate(context)

    @Provides
    @Singleton
    fun provideBillingDataSource(application: Application): BillingDataSource =
        BillingDataSource.getInstance(
            application,
            GlobalScope,
            knownSubscriptionSKUs = BillingRepository.SUBSCRIPTION_SKUS,
            knownInappSKUs = null,
            autoConsumeSKUs = null
        )

    @Provides
    @Singleton
    fun provideBillingRepository(billingDataSource: BillingDataSource): BillingRepository =
        BillingRepository(billingDataSource, GlobalScope)

    @Provides
    @Singleton
    fun provideColorsDataSource(): ColorsDataSource = ColorsDataSource.Base()

    @Provides
    @Singleton
    fun provideFiltersService(retrofitBuilder: ProvideRetrofitBuilder): ProvideFiltersService =
        ProvideFiltersService.Base(retrofitBuilder)

    @Provides
    @Singleton
    fun provideHandleError(): HandleError = HandleDomainError()

    @Provides
    @Singleton
    fun provideFiltersCloudDataSource(
        filtersService: ProvideFiltersService,
        handleError: HandleError
    ): FiltersCloudDataSource =
        FiltersCloudDataSource.Base(filtersService.filtersService(), handleError)

    @Provides
    @Singleton
    fun provideNavigateColor(): NavigateColor = NavigateColor.Base()

    @Provides
    @Singleton
    fun provideFiltersRepository(
        filtersCloudDataSource: FiltersCloudDataSource,
        colorsDataSource: ColorsDataSource,
        resourceProvider: ResourceProvider,
        navigateColor: NavigateColor
    ): CategoriesRepository =
        BaseFiltersRepository(
            filtersCloudDataSource,
            colorsDataSource,
            FiltersCloud.Mapper.Base(GalleryData.Mapper.Base()),
            ColorMapper.Base(resourceProvider, navigateColor)
        )

    @Provides
    @Singleton
    fun provideHomeRepository(
        filtersCloudDataSource: FiltersCloudDataSource,
        wallpapersCache: WallpapersCache.Mutable
    ): HomeRepository =
        BaseHomeRepository(
            filtersCloudDataSource,
            wallpapersCache,
            FiltersCloud.Mapper.Home(GalleryData.Mapper.Base()),
            GalleryData.Mapper.Base()
        )

    @Provides
    @Singleton
    fun provideHomeInteractor(
        homeRepository: HomeRepository,
        dispatchers: Dispatchers,
        galleriesMapper: GalleriesDomain.Mapper<GalleriesUi>,
        mapper: GalleryDomain.Mapper<GalleryUi>,
        navigateCarousel: NavigateCarousel,
        handleError: HandleDomainError
    ): HomeInteractor = HomeInteractor.Base(
        HomesDomain.Mapper.Base(
            HomeDomain.Mapper.Base(
                mapper,
                navigateCarousel
            )
        ),
        galleriesMapper,
        homeRepository,
        dispatchers, handleError
    )

    @Provides
    @Singleton
    fun provideFiltersInteractor(
        filtersRepository: CategoriesRepository,
        navigateFilter: NavigateFilter,
        resourceProvider: ResourceProvider,
        dispatchers: Dispatchers,
        handleError: HandleDomainError
    ): CategoriesInteractor = CategoriesInteractor.Base(
        CategoriesDomain.Mapper.Base(
            CategoryDomain.Mapper.Base(navigateFilter),
            resourceProvider
        ),
        filtersRepository,
        dispatchers, handleError
    )

    @Provides
    @Singleton
    fun provideNavigateFilter(): NavigateFilter = NavigateFilter.Base()

    @Provides
    @Singleton
    fun provideNavigateCarousel(): NavigateCarousel = NavigateCarousel.Base()

    @Provides
    @Singleton
    fun provideColorsCommunication(): ColorsCommunication = ColorsCommunication.Base()

    @Provides
    @Singleton
    fun provideLanguageInterceptor(locale: LocalizationApplicationDelegate): LanguageInterceptor =
        LanguageInterceptor(locale)

    @Provides
    @Singleton
    fun provideResolutionInterceptor(displayProvider: DisplayProvider): ResolutionInterceptor =
        ResolutionInterceptor(displayProvider)

    @Provides
    @Singleton
    fun provideDisplayProvider(context: Application): DisplayProvider =
        DisplayProvider.Base(context)

    @Provides
    @Singleton
    fun provideResourceProvider(context: Application): ResourceProvider =
        ResourceProvider.Base(context)

}