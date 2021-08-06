package wallgram.hd.wallpapers.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import wallgram.hd.wallpapers.model.SubscriptionStatus

@Dao
interface SubscriptionStatusDao {
    @Query("SELECT * FROM subscriptions")
    fun getAll(): LiveData<List<SubscriptionStatus>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(comments: List<SubscriptionStatus>)

    @Query("DELETE FROM subscriptions")
    fun deleteAll()
}
