package wallgram.hd.wallpapers.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "gallery", primaryKeys = ["id", "type"])
@Parcelize
@JsonClass(generateAdapter = true)
data class Gallery(
        val id: Int,
        val width: Int,
        val height: Int,
        val preview: String,
        val original: String? = null,
        val promoted: Int,
        val originalWidth: Int,
        val originalHeight: Int,
        @Embedded
        val links: Links,
        var type: Int = 0 // 0 - Favorite, 1 - History
): Parcelable
