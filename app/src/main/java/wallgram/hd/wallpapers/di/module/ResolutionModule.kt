package wallgram.hd.wallpapers.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.domain.HandleDomainError
import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.core.data.PreferenceDataStore
import wallgram.hd.wallpapers.core.data.ProvideRetrofitBuilder
import wallgram.hd.wallpapers.data.favorites.BaseFavoritesRepository
import wallgram.hd.wallpapers.data.favorites.FavoriteWallpapers
import wallgram.hd.wallpapers.data.favorites.FavoritesCacheDataSource
import wallgram.hd.wallpapers.data.favorites.FavoritesDao
import wallgram.hd.wallpapers.data.gallery.*
import wallgram.hd.wallpapers.data.resolution.*
import wallgram.hd.wallpapers.domain.favorites.FavoritesInteractor
import wallgram.hd.wallpapers.domain.favorites.FavoritesRepository
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryInteractor
import wallgram.hd.wallpapers.domain.gallery.GalleryRepository
import wallgram.hd.wallpapers.domain.resolution.ResolutionRepository
import wallgram.hd.wallpapers.domain.resolution.ResolutionsDomain
import wallgram.hd.wallpapers.domain.resolution.ResolutionsInteractor
import wallgram.hd.wallpapers.presentation.colors.ColorsCommunication
import wallgram.hd.wallpapers.presentation.favorite.ChangeFavorite
import wallgram.hd.wallpapers.presentation.favorite.UpdateFavorites
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.home.HomeViewModel
import wallgram.hd.wallpapers.presentation.settings.resolution.ResolutionsCommunication
import wallgram.hd.wallpapers.presentation.settings.resolution.ResolutionsUi
import wallgram.hd.wallpapers.presentation.settings.resolution.UpdateResolutions
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ResolutionModule {

    @Provides
    @Singleton
    fun provideChangeResolution(
        cacheDataSource: ResolutionCacheDataSource,
        updateResolutions: UpdateResolutions.Update
    ): ChangeResolution = ChangeResolution.Combo(cacheDataSource, updateResolutions)

    @Provides
    @Singleton
    fun provideResolutionCacheDataSource(
        resolutions: ResolutionsCache.Mutable
    ): ResolutionCacheDataSource =
        ResolutionCacheDataSource.Base(resolutions)

    @Provides
    @Singleton
    fun provideResolutionDataSource(): ResolutionsDataSource =
        ResolutionsDataSource.Base()

    @Provides
    fun provideMapper(): Resolution.Mapper<ResolutionsDomain> = Resolution.Mapper.Base()

    @Provides
    fun provideUiMapper(
        cacheDataSource: ResolutionCacheDataSource,
        changeResolution: ChangeResolution,
        resourceProvider: ResourceProvider
    ): ResolutionsDomain.Mapper<ResolutionsUi> =
        ResolutionsDomain.Mapper.Base(cacheDataSource, changeResolution, resourceProvider)

    @Provides
    @Singleton
    fun provideResolutionRepository(
        dataSource: ResolutionsDataSource,
        cacheDataSource: ResolutionCacheDataSource,
        mapper: Resolution.Mapper<ResolutionsDomain>,
        displayProvider: DisplayProvider
    ): ResolutionRepository =
        BaseResolutionRepository(dataSource, cacheDataSource, mapper, displayProvider)

    @Provides
    fun provideResolutionInteractor(
        repository: ResolutionRepository,
        mapper: ResolutionsDomain.Mapper<ResolutionsUi>
    ): ResolutionsInteractor = ResolutionsInteractor.Base(
        repository,
        mapper
    )

    @Provides
    @Singleton
    fun provideResolutions(
        preferenceDataStore: PreferenceDataStore
    ): ResolutionsCache.Mutable = ResolutionsCache.Base(preferenceDataStore)

    @Provides
    @Singleton
    fun provideSaveResolutions(resolutions: ResolutionsCache.Mutable): ResolutionsCache.Save =
        resolutions

    @Provides
    @Singleton
    fun provideReadResolutions(resolutions: ResolutionsCache.Mutable): ResolutionsCache.Read =
        resolutions

    @Provides
    @Singleton
    fun provideUpdateResolutionsObserve(updateResolutions: UpdateResolutions.Base): UpdateResolutions.Observe =
        updateResolutions

    @Provides
    @Singleton
    fun provideUpdateResolutionsUpdate(updateResolutions: UpdateResolutions.Base): UpdateResolutions.Update =
        updateResolutions

    @Provides
    @Singleton
    fun provideUpdateResolutions(): UpdateResolutions.Base = UpdateResolutions.Base()

    @Provides
    @Singleton
    fun provideResolutionsCommunication(): ResolutionsCommunication =
        ResolutionsCommunication.Base()

}