package wallgram.hd.wallpapers.data.favorites

import wallgram.hd.wallpapers.core.Read
import wallgram.hd.wallpapers.core.Save
import wallgram.hd.wallpapers.data.gallery.GalleryCache

interface FavoriteWallpapers {

    interface Save : wallgram.hd.wallpapers.core.Save<GalleryCache.Base>
    interface Read : wallgram.hd.wallpapers.core.Read<List<GalleryCache.Base>>

    interface Mutable : Save, Read

    class Base(private val favoritesDao: FavoritesDao) : Mutable {

        override fun save(data: GalleryCache.Base) = favoritesDao.insert(data)

        override fun read() = favoritesDao.get()

        companion object {
            private const val KEY = "FavoriteCurrenciesKey"
        }
    }
}