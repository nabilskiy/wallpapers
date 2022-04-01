package wallgram.hd.wallpapers.data.tags

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import wallgram.hd.wallpapers.model.SubCategory

@Parcelize
data class Tag(
        override var id: Int = -1,
        override val name: String = "",
        val total: Int = 0,
        override val background: String
): Parcelable, SubCategory()
