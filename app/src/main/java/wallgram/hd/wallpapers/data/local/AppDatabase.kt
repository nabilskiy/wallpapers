package wallgram.hd.wallpapers.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import wallgram.hd.wallpapers.data.favorites.FavoritesDao
import wallgram.hd.wallpapers.data.gallery.GalleryCache
import wallgram.hd.wallpapers.data.local.AppDatabase.Companion.DATABASE_VERSION

@Database(
    entities = [
        GalleryCache.Base::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "akspic.db"
    }

    abstract fun favoritesDao(): FavoritesDao
}