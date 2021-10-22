package wallgram.hd.wallpapers.data.local.dao

import androidx.room.*
import wallgram.hd.wallpapers.model.Gallery
import kotlinx.coroutines.flow.Flow

@Dao
interface GalleryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(gallery: Gallery): Long

    @Update()
    suspend fun update(gallery: Gallery)

    @Transaction
    suspend fun insertOrUpdate(gallery: Gallery) {
        if (insertIgnore(gallery) == -1L) {
            update(gallery)
        }
    }

    @Transaction
    suspend fun addToFavorites(gallery: Gallery) {
        if (insertIgnore(gallery) == -1L) {
            delete(gallery)
        }
    }

    @Query("SELECT EXISTS(SELECT * FROM gallery WHERE id = :id AND type == 0)")
    fun isFavorite(id: Int): Boolean

    @Query("SELECT * FROM gallery")
    fun getAll(): Flow<MutableList<Gallery>>

    @Query("SELECT * FROM gallery WHERE type == :type")
    fun getAllWithType(type: Int): Flow<List<Gallery>>

    @Query("DELETE FROM gallery WHERE type == :type")
    suspend fun deleteAll(type: Int)

    @Delete
    suspend fun delete(gallery: Gallery)
}