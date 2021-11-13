package wallgram.hd.wallpapers.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Tag(
        override var id: Int = -1,
        override val name: String = "",
        val total: Int? = 0,
        override val background: String? = null
): Parcelable, SubCategory()
