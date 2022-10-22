package wallgram.hd.wallpapers.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.domain.HandleDomainError
import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.core.data.ProvideRetrofitBuilder
import wallgram.hd.wallpapers.data.favorites.BaseFavoritesRepository
import wallgram.hd.wallpapers.data.favorites.FavoriteWallpapers
import wallgram.hd.wallpapers.data.favorites.FavoritesCacheDataSource
import wallgram.hd.wallpapers.data.favorites.FavoritesDao
import wallgram.hd.wallpapers.data.gallery.*
import wallgram.hd.wallpapers.domain.favorites.FavoritesInteractor
import wallgram.hd.wallpapers.domain.favorites.FavoritesRepository
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryInteractor
import wallgram.hd.wallpapers.domain.gallery.GalleryRepository
import wallgram.hd.wallpapers.presentation.favorite.ChangeFavorite
import wallgram.hd.wallpapers.presentation.favorite.UpdateFavorites
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.home.HomeViewModel
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FavoritesModule {

    @Provides
    @Singleton
    fun provideChangeFavorite(cacheDataSource: FavoritesCacheDataSource): ChangeFavorite = ChangeFavorite.Combo(cacheDataSource, UpdateFavorites.Base())

    @Provides
    @Singleton
    fun provideFavoritesCacheDataSource(
        favorites: FavoriteWallpapers.Mutable,
        favoritesDao: FavoritesDao,
        handleError: HandleError
    ): FavoritesCacheDataSource =
        FavoritesCacheDataSource.Base(favorites, favoritesDao, handleError)

    @Provides
    @Singleton
    fun provideFavoritesRepository(cacheDataSource: FavoritesCacheDataSource, wallpapersCache: WallpapersCache.Mutable): FavoritesRepository =
        BaseFavoritesRepository(cacheDataSource, GalleryCache.Mapper.Base(), GalleryData.Mapper.Base(), wallpapersCache)

    @Provides
    fun provideFavoritesInteractor(
        repository: GalleryRepository,
        dispatchers: Dispatchers,
        handleError: HandleDomainError,
        mapper: GalleryDomain.Mapper<GalleryUi>
    ): FavoritesInteractor = FavoritesInteractor.Base(
        GalleriesDomain.Mapper.Favorites(mapper),
        repository,
        dispatchers, handleError
    )

    @Provides
    @Singleton
    fun provideFavorites(
        favoritesDao: FavoritesDao
    ): FavoriteWallpapers.Mutable = FavoriteWallpapers.Base(favoritesDao)

    @Provides
    @Singleton
    fun provideSaveFavorites(favorites: FavoriteWallpapers.Mutable): FavoriteWallpapers.Save =
        favorites

    @Provides
    @Singleton
    fun provideReadFavorites(favorites: FavoriteWallpapers.Mutable): FavoriteWallpapers.Read =
        favorites

    @Provides
    @Singleton
    fun provideUpdateFavoritesObserve(updateFavorites: UpdateFavorites.Base): UpdateFavorites.Observe = updateFavorites

    @Provides
    @Singleton
    fun provideUpdateFavoritesUpdate(updateFavorites: UpdateFavorites.Base): UpdateFavorites.Update = updateFavorites

    @Provides
    @Singleton
    fun provideUpdateFavorites(): UpdateFavorites.Base = UpdateFavorites.Base()

}