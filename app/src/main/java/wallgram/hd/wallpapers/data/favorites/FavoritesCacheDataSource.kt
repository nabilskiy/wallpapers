package wallgram.hd.wallpapers.data.favorites

import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.core.data.CacheDataSource
import wallgram.hd.wallpapers.data.gallery.GalleryCache
import wallgram.hd.wallpapers.presentation.favorite.ChangeFavorite

interface FavoritesCacheDataSource : ChangeFavorite, IsFavorite {

    suspend fun favorites(): List<GalleryCache>
    suspend fun history(): List<GalleryCache>

    class Base(
        private val favorites: FavoriteWallpapers.Mutable,
        private val favoritesDao: FavoritesDao,
        handleError: HandleError
    ) : CacheDataSource.Abstract(handleError), FavoritesCacheDataSource {

        override suspend fun favorites() = favoritesDao.get()
        override suspend fun history() = favoritesDao.getHistory()
        override fun changeFavorite(item: GalleryCache.Base): Boolean =
            favoritesDao.addToFavorites(item)

        override fun isFavorite(id: Int) = favoritesDao.isFavorite(id)
    }

}