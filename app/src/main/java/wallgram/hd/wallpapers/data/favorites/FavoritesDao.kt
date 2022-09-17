package wallgram.hd.wallpapers.data.favorites

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import wallgram.hd.wallpapers.data.gallery.GalleryCache


@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(gallery: GalleryCache.Base): Long

    @Insert
    fun insert(item: GalleryCache.Base)

    @Update
    suspend fun update(gallery: GalleryCache.Base)

    @Transaction
    suspend fun insertOrUpdate(gallery: GalleryCache.Base) {
        if (insertIgnore(gallery) == -1L) {
            update(gallery)
        }
    }

    @Transaction
     fun addToFavorites(item: GalleryCache.Base): Boolean {
        if (insertIgnore(item) == -1L) {
            delete(item)
            return false
        }
        return !item.isHistory
    }

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE id = :id AND isHistory = 0)")
    fun isFavorite(id: Int): Boolean

    @Query("SELECT * FROM favorites WHERE isHistory = 0")
    fun get(): List<GalleryCache.Base>

    @Query("SELECT * FROM favorites WHERE isHistory = 1")
    fun getHistory(): List<GalleryCache.Base>

    @Query("DELETE FROM favorites")
    suspend fun deleteAll()

    @Delete
    fun delete(gallery: GalleryCache.Base)
}