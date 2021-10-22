package wallgram.hd.wallpapers.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import wallgram.hd.wallpapers.data.local.Database.Companion.DATABASE_VERSION
import wallgram.hd.wallpapers.data.local.converters.DateConverter
import wallgram.hd.wallpapers.data.local.dao.GalleryDao
import wallgram.hd.wallpapers.model.Gallery


@Database(
        entities = [
            Gallery::class
        ],
        version = DATABASE_VERSION,
        exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class Database : RoomDatabase() {

    companion object {
        const val DATABASE_VERSION = 4
        const val DATABASE_NAME = "akspic.db"
    }

    abstract fun galleryDao(): GalleryDao
}