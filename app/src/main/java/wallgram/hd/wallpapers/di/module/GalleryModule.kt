package wallgram.hd.wallpapers.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.domain.HandleDomainError
import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.core.data.ProvideRetrofitBuilder
import wallgram.hd.wallpapers.data.favorites.FavoritesCacheDataSource
import wallgram.hd.wallpapers.data.gallery.*
import wallgram.hd.wallpapers.domain.gallery.*
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
    fun provideGalleryRepository(
        galleryCloudDataSource: GalleryCloudDataSource,
        wallpapersCache: WallpapersCache.Mutable
    ): GalleryRepository =
        BaseGalleryRepository(
            galleryCloudDataSource,
            wallpapersCache,
            GalleryCloud.Mapper.Base(),
            GalleryData.Mapper.Base()
        )

    @Provides
    @Singleton
    fun provideNavigateGallery(
        updateSave: UpdateSave.Update
    ): NavigateGallery = NavigateGallery.Base(updateSave)

    @Provides
    fun provideGalleryMapper(
        cacheDataSource: FavoritesCacheDataSource,
        updateFavorites: UpdateFavorites.Update
    ): GalleryDomain.Mapper<GalleryUi> {
        return GalleryDomain.Mapper.Base(
            cacheDataSource,
            ChangeFavorite.Combo(cacheDataSource, updateFavorites)
        )
    }

    @Provides
    fun provideGalleriesMapper(mapper: GalleryDomain.Mapper<GalleryUi>): GalleriesDomain.Mapper<GalleriesUi> =
        GalleriesDomain.Mapper.Base(mapper)

    @Provides
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
    fun provideSimilarInteractor(
        galleryRepository: GalleryRepository,
        dispatchers: Dispatchers,
        handleError: HandleDomainError,
        mapper: GalleryDomain.Mapper<GalleryUi>,
        resourceProvider: ResourceProvider
    ): SimilarInteractor = SimilarInteractor.Base(
        GalleriesDomain.Mapper.Similar(mapper, resourceProvider),
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
    fun provideUpdateDownloadObserve(updateDownload: UpdateDownload.Base): UpdateDownload.Observe = updateDownload

    @Provides
    @Singleton
    fun provideUpdateDownloadUpdate(updateDownload: UpdateDownload.Base): UpdateDownload.Update = updateDownload

    @Provides
    @Singleton
    fun provideUpdateDownload(): UpdateDownload.Base = UpdateDownload.Base()

}