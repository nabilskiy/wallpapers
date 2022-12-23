package wallgram.hd.wallpapers.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.domain.HandleDomainError
import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.core.data.ProvideRetrofitBuilder
import wallgram.hd.wallpapers.data.SubscriptionsDataSource
import wallgram.hd.wallpapers.data.ads.recyclerbanner.RecyclerBannerAd
import wallgram.hd.wallpapers.data.favorites.FavoritesCacheDataSource
import wallgram.hd.wallpapers.data.filters.FiltersCloud
import wallgram.hd.wallpapers.data.filters.FiltersCloudDataSource
import wallgram.hd.wallpapers.data.gallery.*
import wallgram.hd.wallpapers.di.qualifiers.CarouselAdBanner
import wallgram.hd.wallpapers.di.qualifiers.CarouselMapper
import wallgram.hd.wallpapers.di.qualifiers.SearchInteractor
import wallgram.hd.wallpapers.di.qualifiers.SearchMapper
import wallgram.hd.wallpapers.domain.gallery.*
import wallgram.hd.wallpapers.domain.home.HomeRepository
import wallgram.hd.wallpapers.presentation.dialogs.UpdateDownload
import wallgram.hd.wallpapers.presentation.favorite.ChangeFavorite
import wallgram.hd.wallpapers.presentation.favorite.UpdateFavorites
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.gallery.NavigateGallery
import wallgram.hd.wallpapers.presentation.home.HomeViewModel
import wallgram.hd.wallpapers.presentation.wallpapers.UpdateSave
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GalleryModule {

    @Provides
    @Singleton
    fun provideGalleryService(retrofitBuilder: ProvideRetrofitBuilder): ProvideGalleryService =
        ProvideGalleryService.Base(retrofitBuilder)

    @Provides
    @Singleton
    fun provideGalleryCloudDataSource(
        galleryService: ProvideGalleryService,
        handleError: HandleError
    ): GalleryCloudDataSource =
        GalleryCloudDataSource.Base(galleryService.galleryService(), handleError)

    @Provides
    @Singleton
    fun provideGalleryRepository(
        cloudDataSource: GalleryCloudDataSource,
        cacheDataSource: FavoritesCacheDataSource,
        filtersCloudDataSource: FiltersCloudDataSource,
        wallpapersCache: WallpapersCache.Mutable
    ): GalleryRepository =
        BaseGalleryRepository(
            cloudDataSource,
            cacheDataSource,
            filtersCloudDataSource,
            FiltersCloud.Mapper.Home(GalleryData.Mapper.Base()),
            wallpapersCache,
            GalleryCloud.Mapper.Base(),
            GalleryCache.Mapper.Base(),
            GalleryData.Mapper.Base()
        )

    @Provides
    @Singleton
    fun provideNavigateGallery(
        updateSave: UpdateSave.Update,
        saveSelect: SaveSelect,
    ): NavigateGallery = NavigateGallery.Base(saveSelect, updateSave)

    @Provides
    fun provideSaveSelect(repository: GalleryRepository): SaveSelect = repository

    @Provides
    fun provideGalleryMapper(
        cacheDataSource: FavoritesCacheDataSource,
        updateFavorites: UpdateFavorites.Update,
        navigateGallery: NavigateGallery
    ): GalleryDomain.Mapper<GalleryUi> {
        return GalleryDomain.Mapper.Base(
            cacheDataSource,
            ChangeFavorite.Combo(cacheDataSource, updateFavorites),
            navigateGallery,
        )
    }

    @Provides
    fun provideGalleriesMapper(
        mapper: GalleryDomain.Mapper<GalleryUi>,
        bannerAd: RecyclerBannerAd,
        subscriptionsDataSource: SubscriptionsDataSource
    ): GalleriesDomain.Mapper<GalleriesUi> =
        GalleriesDomain.Mapper.Base(mapper, bannerAd, subscriptionsDataSource)

    @Provides
    @CarouselMapper
    fun provideGalleriesCarouselMapper(
        mapper: GalleryDomain.Mapper<GalleryUi>,
        @CarouselAdBanner bannerAd: RecyclerBannerAd,
        subscriptionsDataSource: SubscriptionsDataSource
    ): GalleriesDomain.Mapper<GalleriesUi> =
        GalleriesDomain.Mapper.Carousel(mapper, bannerAd, subscriptionsDataSource)

    @Provides
    @SearchMapper
    fun provideGalleriesSearchMapper(
        mapper: GalleryDomain.Mapper<GalleryUi>,
        bannerAd: RecyclerBannerAd,
        subscriptionsDataSource: SubscriptionsDataSource
    ): GalleriesDomain.Mapper<GalleriesUi> =
        GalleriesDomain.Mapper.Search(mapper, bannerAd, subscriptionsDataSource)


    @Provides
    fun provideRecyclerAdBanner(
        context: Context,
        displayProvider: DisplayProvider,
        subscriptionsDataSource: SubscriptionsDataSource
    ): RecyclerBannerAd = RecyclerBannerAd.Base(context, displayProvider, subscriptionsDataSource)

    @Provides
    @CarouselAdBanner
    fun provideCarouselAdBanner(
        context: Context,
        displayProvider: DisplayProvider,
        subscriptionsDataSource: SubscriptionsDataSource
    ): RecyclerBannerAd =
        RecyclerBannerAd.Carousel(context, displayProvider, subscriptionsDataSource)

    @Provides
    @Singleton
    fun provideGalleryInteractor(
        galleryRepository: GalleryRepository,
        dispatchers: Dispatchers,
        handleError: HandleDomainError,
        galleriesMapper: GalleriesDomain.Mapper<GalleriesUi>
    ): GalleryInteractor = GalleryInteractor.Base(
        galleriesMapper,
        galleryRepository,
        dispatchers, handleError
    )

    @Provides
    @Singleton
    @SearchInteractor
    fun provideGallerySearchInteractor(
        galleryRepository: GalleryRepository,
        dispatchers: Dispatchers,
        handleError: HandleDomainError,
        @SearchMapper
        galleriesMapper: GalleriesDomain.Mapper<GalleriesUi>
    ): GalleryInteractor = GalleryInteractor.Search(
        galleriesMapper,
        galleryRepository,
        dispatchers, handleError
    )

    @Provides
    @Singleton
    fun provideUpdateSaveObserve(updateSave: UpdateSave.Base): UpdateSave.Observe = updateSave

    @Provides
    @Singleton
    fun provideUpdateSaveUpdate(updateSave: UpdateSave.Base): UpdateSave.Update = updateSave

    @Provides
    @Singleton
    fun provideUpdateSave(): UpdateSave.Base = UpdateSave.Base()

    @Provides
    @Singleton
    fun provideUpdateDownloadObserve(updateDownload: UpdateDownload.Base): UpdateDownload.Observe =
        updateDownload

    @Provides
    @Singleton
    fun provideUpdateDownloadUpdate(updateDownload: UpdateDownload.Base): UpdateDownload.Update =
        updateDownload

    @Provides
    @Singleton
    fun provideUpdateDownload(): UpdateDownload.Base = UpdateDownload.Base()

}