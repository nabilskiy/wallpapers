package wallgram.hd.wallpapers.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
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
import wallgram.hd.wallpapers.data.history.BaseHistoryRepository
import wallgram.hd.wallpapers.domain.favorites.FavoritesInteractor
import wallgram.hd.wallpapers.domain.favorites.FavoritesRepository
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryInteractor
import wallgram.hd.wallpapers.domain.gallery.GalleryRepository
import wallgram.hd.wallpapers.domain.history.HistoryInteractor
import wallgram.hd.wallpapers.domain.history.HistoryRepository
import wallgram.hd.wallpapers.presentation.favorite.ChangeFavorite
import wallgram.hd.wallpapers.presentation.favorite.UpdateFavorites
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.home.HomeViewModel
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class HistoryModule {

    @Provides
    @ViewModelScoped
    fun provideHistoryRepository(cacheDataSource: FavoritesCacheDataSource, wallpapersCache: WallpapersCache.Mutable): HistoryRepository =
        BaseHistoryRepository(cacheDataSource, GalleryCache.Mapper.Base(), GalleryData.Mapper.Base(), wallpapersCache)

    @Provides
    @ViewModelScoped
    fun provideHistoryInteractor(
        historyRepository: HistoryRepository,
        dispatchers: Dispatchers,
        handleError: HandleDomainError,
        mapper: GalleryDomain.Mapper<GalleryUi>
    ): HistoryInteractor = HistoryInteractor.Base(
        GalleriesDomain.Mapper.History(mapper),
        historyRepository,
        dispatchers, handleError
    )

}