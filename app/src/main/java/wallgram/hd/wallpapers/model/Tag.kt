package wallgram.hd.wallpapers.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tag(
        override var id: Int = -1,
        override val name: String = "",
        val total: Int? = 0,
        override val background: String? = null
): Parcelable, SubCategory()
