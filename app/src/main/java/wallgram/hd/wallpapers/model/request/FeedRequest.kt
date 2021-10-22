package wallgram.hd.wallpapers.model.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import wallgram.hd.wallpapers.ui.wallpapers.WallType

@Parcelize
data class FeedRequest(
        var type: WallType = WallType.ALL,
        var category: Int = -1,
        var categoryName: String = "",
        var sort: String = "",
        var page: Int = 1,
        var search: String = "",
        var r: String? = null,
        var g: String? = null,
        var b: String? = null
): Parcelable
